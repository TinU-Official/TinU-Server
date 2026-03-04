package com.tinuproject.tinu.domain.post.service

import com.tinuproject.tinu.domain.post.repository.CategoryRepository
import com.tinuproject.tinu.infra.s3.exception.UploadSizeOutOfRangeException
import com.tinuproject.tinu.domain.post.exception.ScrapAlreadyExistException
import com.tinuproject.tinu.domain.post.exception.ScrapNotFoundException
import com.tinuproject.tinu.domain.post.repository.HashTagRepository
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.domain.post.controller.dto.request.PostCreateRequest
import com.tinuproject.tinu.domain.post.controller.dto.request.PostDeleteRequest
import com.tinuproject.tinu.domain.post.controller.dto.request.PostUpdateRequest
import com.tinuproject.tinu.domain.post.repository.PostHashTagMapRepository
import com.tinuproject.tinu.domain.post.repository.ScrapRepository
import com.tinuproject.tinu.infra.s3.service.S3Service
import com.tinuproject.tinu.domain.post.entity.Multimedia
import com.tinuproject.tinu.domain.post.controller.dto.response.*
import com.tinuproject.tinu.domain.post.entity.Post
import com.tinuproject.tinu.domain.post.entity.HashTag
import com.tinuproject.tinu.domain.post.entity.PostHashTagMap
import com.tinuproject.tinu.domain.post.repository.PostQueryRepository
import com.tinuproject.tinu.domain.post.repository.PostRepository
import com.tinuproject.tinu.domain.post.entity.Scrap
import com.tinuproject.tinu.domain.post.exception.*
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

const val SIZE = 20
@Service
class PostServiceImpl(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
    private val postQueryRepository: PostQueryRepository,
    private val categoryRepository: CategoryRepository,
    private val hashTagRepository: HashTagRepository,
    private val multimediaRepository: MultimediaRepository,
    private val postHashTagMapRepository: PostHashTagMapRepository,
    private val scrapRepository: ScrapRepository,
    private val s3Service: S3Service,
): PostService {
    val log : Logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    override fun getPostList(
            userId: UUID,
            cursorId: String?,
            keyword: String?,
            category: List<Long>?,
            minPrice: Int?,
            maxPrice: Int?,
            onlySell: Boolean
    ): PostsListResponse {
        val member = memberRepository.findMemberByUserId(userId)!!

        val university = member.university!!

        log.info("University ID: ${university.id}")
        log.info("Params: cursorId=$cursorId, keyword=$keyword, category=$category, minPrice=$minPrice, maxPrice=$maxPrice, onlySell=$onlySell")

        var rawPosts = postQueryRepository.findPosts(
                university,
                cursorId,
                SIZE.toLong(),
                keyword,
                category,
                minPrice,
                maxPrice,
                onlySell
        )

        var nextCursorId = ""
        if (rawPosts.size > SIZE) {
            rawPosts = rawPosts.subList(0, SIZE)
            nextCursorId = rawPosts.last().id.toString()
        }

        val scrapPostIds = scrapRepository.findPostIdsByMemberId(member.id!!)

        val posts = rawPosts.map { post ->
            PostListBodyResponse(
                id = post.id!!,
                createdAt = post.createdAt!!,
                title = post.title,
                price = post.price,
                thumbnail = post.thumbnail,
                isLike = post.id in scrapPostIds,
                likeCount = post.likeCount,
                isSoldOut = post.isSoldOut
            )
        }

        return PostsListResponse(
                posts = posts,
                size = posts.size,
                nextCursorId = nextCursorId
        )
    }

    @Transactional(readOnly = true)
    override fun getPostDetail(userId: UUID, postId: Long): PostDetailResponse {
        val member = memberRepository.findMemberByUserId(userId)!!

        val post = postRepository.findPostById(postId)
                ?: throw PostNotFoundException()

        if (member.university!!.id != post.university.id) {
            throw UniversityNotMatchException()
        }

        if (post.isHide) {
            throw PostHiddenException()
        }

        return PostDetailResponse(
                postId = post.id!!,
                date = post.createdAt!!,
                title = post.title,
                body = post.body,
                memberId = post.author.id!!,
                nickname = post.author.nickname!!,
                profile = post.author.profileImageURL,
                categoryId = post.category.id!!,
                price = post.price,
                sellMethod = post.sellMethod.toSet(),
                paymentMethod = post.paymentMethod.toSet(),
                isSoldOut = post.isSoldOut,
                isLike = member.scrap.any { it.post == post },
                likeCount = post.scrapCount,
                isWriter = member == post.author,
                images = post.multimedia.map { it.url },
                hashTags = post.postHashTagMap.map {
                    PostHashTagResponse(
                            id = it.hashTag.id!!,
                            name = it.hashTag.tagName
                    )
                }
        )
    }

    @Transactional
    override fun createPost(userId: UUID, postCreateRequest: PostCreateRequest): PostCreateResponse {
        val member = memberRepository.findMemberByUserId(userId)!!

        val category = categoryRepository.findCategoryById(postCreateRequest.categoryId)
                ?: throw CategoryNotFoundException()

            log.info("이미지 검증")

        if (postCreateRequest.images.size !in 0..10)
            throw UploadSizeOutOfRangeException()

        val urlList = runBlocking {
            s3Service.verifyImages(postCreateRequest.images)
        }

        val newPost = Post(
                university = member.university!!,
                title = postCreateRequest.title,
                body = postCreateRequest.body,
                author = member,
                buyer = null,
                category = category,
                price = postCreateRequest.price,
                sellMethod = postCreateRequest.sellMethod,
                isSoldOut = false,
                isHide = false,
                paymentMethod = postCreateRequest.paymentMethod,
                thumbnail = urlList.firstOrNull(),
                reportCount = 0,
                scrapCount = 0,
                multimedia = mutableListOf(),
                postHashTagMap = mutableListOf(),
                scrap = mutableListOf()
        )

        log.info("게시글 추가")
        postRepository.save(newPost)

        log.info("이미지 추가")
        mappingMultimedia(newPost, urlList.toMutableList())

        log.info("해시태그맵 추가")
        mappingPostHashTagMap(newPost, postCreateRequest.hashTags.toMutableList())

        return PostCreateResponse(postId = newPost.id!!)
    }

    @Transactional
    override fun updatePost(userId: UUID, postId: Long, postUpdateRequest: PostUpdateRequest) {

        log.info("게시글 검증")
        val post = postRepository.findPostById(postId)
                ?: throw PostNotFoundException()

        if (userId != post.author.userId)
            throw AuthorNotMatchException()

        log.info("카테고리 검증")
        val category = categoryRepository.findCategoryById(postUpdateRequest.categoryId)
                ?: throw CategoryNotFoundException()

        log.info("이미지 검증")
        if (postUpdateRequest.images.size !in 0..10)
            throw UploadSizeOutOfRangeException()

        var urlList = emptyList<String>()
        if (postUpdateRequest.images.isNotEmpty())
            urlList = runBlocking { s3Service.verifyImages(postUpdateRequest.images) }

        log.info("이미지 삭제")
        if (post.multimedia.isNotEmpty())
            s3Service.removeImages(post.multimedia.map { it.url })

        multimediaRepository.deleteAllByPostId(postId)

        log.info("이미지 추가")
        mappingMultimedia(post, urlList.toMutableList())

        log.info("해시태그맵 추가")
        postHashTagMapRepository.deleteAllByPostId(postId)
        mappingPostHashTagMap(post, postUpdateRequest.hashTags.toMutableList())

        log.info("게시글 업데이트")
        post.updatePost(postUpdateRequest, category, urlList.firstOrNull())

        log.info("게시글 저장")
        postRepository.save(post)

    }

    @Transactional
    override fun deletePost(userId: UUID, postDeleteRequest: PostDeleteRequest) {

        log.info("게시글 검증")
        val post = postRepository.findPostById(postDeleteRequest.postId)
                ?: throw PostNotFoundException()

        if (userId != post.author.userId)
            throw AuthorNotMatchException()

        log.info("이미지 삭제")
        if (post.multimedia.isNotEmpty())
            s3Service.removeImages(post.multimedia.map { it.url })

        postRepository.deleteById(postDeleteRequest.postId)

    }

    private fun mappingMultimedia(post: Post, urls : MutableList<String>) {
        val images = urls.map { url ->
            Multimedia(url = url, isImage = false, post = post)
        }

        multimediaRepository.saveAll(images)
    }

    private fun mappingPostHashTagMap(post: Post, hashTagList: MutableList<String>) {
        val existingTags = hashTagRepository.findAllByTagNameIn(hashTagList)
        val existingTagNames = existingTags.map { it.tagName }

        val newTags = hashTagList
                .filter { tagName -> !existingTagNames.contains(tagName) }
                .map { tagName -> HashTag(tagName = tagName) }

        if (newTags.isNotEmpty()) {
            hashTagRepository.saveAll(newTags)
        }

        //newTagMaps에는 hashTagList 순서대로 existingTags와 saveNewTags에 있는 HashTag 엔티티가 들어간다.
        val newTagMaps: List<HashTag> = hashTagList.map { tagName ->
            existingTags.find { it.tagName == tagName } ?: newTags.find { it.tagName == tagName }!!
        }

        val postHashTagMaps = newTagMaps.map { hashTag ->
            PostHashTagMap(post = post, hashTag = hashTag)
        }

        postHashTagMapRepository.saveAll(postHashTagMaps)
    }

    @Transactional
    override fun createPostScrap(userId: UUID, postId: Long) {
        val member = memberRepository.findMemberByUserId(userId)!!

        val post = postRepository.findPostById(postId)
                ?: throw PostNotFoundException()

        if (member.university!!.id != post.university.id) {
            throw UniversityNotMatchException()
        }

        val scrap = scrapRepository.findScrapByMemberIdAndPostId(member.id!!, postId)
        if (scrap != null)
            throw ScrapAlreadyExistException()

        scrapRepository.save(Scrap(member = member, post = post))
        post.scrapCount++
        postRepository.save(post)
    }

    @Transactional
    override fun deletePostScrap(userId: UUID, postId: Long) {
        val member = memberRepository.findMemberByUserId(userId)!!

        val scrap = scrapRepository.findScrapByMemberIdAndPostId(member.id!!, postId)
                ?: throw ScrapNotFoundException()

        scrapRepository.delete(scrap)
        scrap.post.scrapCount--
        postRepository.save(scrap.post)
    }

    @Transactional
    override fun updatePostStatus(userId: UUID, postId: Long, isSoldOut: Boolean) {

        log.info("게시글 검증")
        val post = postRepository.findPostById(postId)
                ?: throw PostNotFoundException()

        if (userId != post.author.userId)
            throw AuthorNotMatchException()

        post.isSoldOut = isSoldOut
        postRepository.save(post)
    }

    @Transactional
    override fun updatePostHide(userId: UUID, postId: Long, isHide: Boolean) {

        log.info("게시글 검증")
        val post = postRepository.findPostById(postId)
                ?: throw PostNotFoundException()

        if (userId != post.author.userId)
            throw AuthorNotMatchException()

        post.isHide = isHide
        postRepository.save(post)
    }

}