package com.tinuproject.tinu.domain.member.service

import com.tinuproject.tinu.annotation.ServiceTest
import com.tinuproject.tinu.domain.member.entity.Member
import com.tinuproject.tinu.domain.member.enums.Evaluation
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.domain.member.repository.ReviewRepository
import com.tinuproject.tinu.domain.member.service.dto.input.CreateReviewInput
import com.tinuproject.tinu.domain.member.service.dto.input.SearchWriteReviewInput
import com.tinuproject.tinu.domain.post.exception.PostNotFoundException
import com.tinuproject.tinu.domain.post.repository.CategoryRepository
import com.tinuproject.tinu.domain.post.repository.PostRepository
import com.tinuproject.tinu.domain.university.entity.University
import com.tinuproject.tinu.domain.university.repository.UniversityRepository
import com.tinuproject.tinu.factory.TestCategoryFactory
import com.tinuproject.tinu.factory.TestMemberFactory
import com.tinuproject.tinu.factory.TestPostFactory
import com.tinuproject.tinu.factory.TestUniversityFactory
import com.tinuproject.tinu.global.exception.ForbiddenException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import kotlin.test.Test


import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows

// 로컬/CI 환경변수(application properties placeholder) 미설정으로 Spring context 로딩이 실패한다.
// 환경 정리 후 다시 활성화 예정.
@Disabled("환경변수(application placeholder) 미설정으로 Spring context 로딩 실패. 환경 정리 후 활성화 예정.")
@ServiceTest
class ReviewServiceImplTest(
) {

    @Autowired
    private lateinit var reviewService: ReviewService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var universityRepository: UniversityRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var reviewRepository: ReviewRepository

    @Autowired
    private lateinit var categoryRepository: CategoryRepository


    @AfterEach
    fun tearDown() {
        memberRepository.deleteAllInBatch()
        postRepository.deleteAllInBatch()
        reviewRepository.deleteAllInBatch()
        universityRepository.deleteAllInBatch()
        categoryRepository.deleteAll()
    }



    private fun createMember(university: University) : Member {

        return TestMemberFactory.create(
            memberRepository = memberRepository,
            university = university,
            nickname = "테스트",
            userId = UUID.randomUUID()
        )
    }


    @Test
    @DisplayName("사용자가 리뷰를 남겨야한다면 true 아니라면 false를 반환한다.")
    fun needWrittenReviewTest(){
        //Given
        val testUniversity = TestUniversityFactory.create(universityRepository)

        //유저 정보 기입
        val reviewer = createMember(testUniversity)

        val reviewee = createMember(testUniversity)

        val badUser = createMember(testUniversity)

        val reviewerId = reviewer.userId

        val badUserId = badUser.userId
        //게시글 정보 기입
        val testCategory = TestCategoryFactory.create(categoryRepository = categoryRepository)

        val post = TestPostFactory.create(
            postRepository = postRepository,
            author = reviewee,
            buyer = reviewer,
            university = testUniversity,
            category = testCategory
        )
        //When & Then
        assertThat(reviewService.needWrittenReview(
            SearchWriteReviewInput(userId = reviewerId,
                postId = post.id!!)
        )).isTrue()

        reviewService.createReview(
            input = CreateReviewInput(
                reviewerId = reviewerId,
                postId = post.id!!,
                mainEvaluation = Evaluation.GOOD,
                isFriendly = true,
                notLate = false,
                respondedQuickly = true
            )
        )

        assertThat(reviewService.needWrittenReview(
            SearchWriteReviewInput(userId = reviewerId,
                postId = post.id!!)
        )).isFalse()


        assertThat(reviewService.needWrittenReview(
            SearchWriteReviewInput(userId = badUserId,
                postId = post.id!!)
        )).isFalse()

    }



    @Test
    @DisplayName("거래 리뷰를 남기려할때 Reviewer Reviewee Post에 대한 정보가 모두 맞을 때 결과 true 반환.")
    fun createReviewTest(){
        //Given
        val testUniversity = TestUniversityFactory.create(universityRepository)

        //유저 정보 기입
        val reviewer = createMember(testUniversity)

        val reviewee = createMember(testUniversity)

        val reviewerId = reviewer.userId

        //게시글 정보 기입
        val testCategory = TestCategoryFactory.create(categoryRepository = categoryRepository)

        val post = TestPostFactory.create(
            postRepository = postRepository,
            author = reviewee,
            buyer = reviewer,
            university = testUniversity,
            category = testCategory
        )
        //When
        val result  = reviewService.createReview(
            input = CreateReviewInput(
                reviewerId = reviewerId,
                postId = post.id!!,
                mainEvaluation = Evaluation.GOOD,
                isFriendly = true,
                notLate = false,
                respondedQuickly = true
            )
        )

        //Then
        assertThat(result).isTrue()

        val resultReviewee = memberRepository.findMemberByUserId(reviewee.userId)

        assertThat(resultReviewee!!.reviewSummary)
            .extracting("isFriendlyNum","notLateNum","respondedQuicklyNum")
            .containsExactly(1, -1, 1)

    }

    @Test
    @DisplayName("리뷰 작성 시 피평가자의 mark를 이번 결과에 반영하여 업데이트 한다.")
    fun createReviewUpdateMarkTest(){
        //given
        val testUniversity = TestUniversityFactory.create(universityRepository)

        //유저 정보 기입
        val reviewer = createMember(testUniversity)

        var reviewee = createMember(testUniversity)

        val reviewerId = reviewer.userId

        //게시글 정보 기입
        val testCategory = TestCategoryFactory.create(categoryRepository = categoryRepository)

        val post = TestPostFactory.create(
            postRepository = postRepository,
            author = reviewee,
            buyer = reviewer,
            university = testUniversity,
            category = testCategory
        )
        val post2 = TestPostFactory.create(
            postRepository = postRepository,
            author = reviewee,
            buyer = reviewer,
            university = testUniversity,
            category = testCategory
        )

        //When
       reviewService.createReview(
           input = CreateReviewInput(
                reviewerId = reviewerId,
                postId = post.id!!,
                mainEvaluation = Evaluation.GOOD,
                isFriendly = true,
                notLate = false,
                respondedQuickly = true
            )
        )
       reviewService.createReview(
           input = CreateReviewInput(
                reviewerId = reviewerId,
                postId = post2.id!!,
                mainEvaluation = Evaluation.SOSO,
                isFriendly = true,
                notLate = false,
                respondedQuickly = true
            )
        )
        //when & then
        reviewee = memberRepository.findMemberByUserId(reviewee.userId)!!

        assertThat(reviewee.reviewSummary!!.mark)
            .isEqualTo((Evaluation.GOOD.score+Evaluation.SOSO.score) / 2)

        assertThat(reviewee.reviewSummary!!.reviewCount)
            .isEqualTo(2)

    }


    @Test
    @DisplayName("리뷰 작성 시 게시글의 구매 설정이 완료되지 않거나, 작성자가 해당 게시글 실 거래자가 아닌 경우 UnAuthorization Exception 반환")
    fun createReviewUnAuthorizationExceptionTest(){
        //Given
        val testUniversity = TestUniversityFactory.create(universityRepository)

        //유저 정보 기입
        val reviewer = createMember(testUniversity)

        val reviewee = createMember(testUniversity)

        val badUser = createMember(testUniversity)

        val reviewerId = reviewer.userId

        //게시글 정보 기입
        val testCategory = TestCategoryFactory.create(categoryRepository = categoryRepository)

        val post = TestPostFactory.create(
            postRepository = postRepository,
            author = reviewee,
            buyer = reviewer,
            university = testUniversity,
            category = testCategory
        )

        val notTradePost = TestPostFactory.create(
            postRepository = postRepository,
            author = reviewer,
            university = testUniversity,
            category = testCategory,
            buyer =null
        )

        val badUserCase =  CreateReviewInput(
            reviewerId = badUser.userId,
            postId = post.id!!,
            mainEvaluation = Evaluation.GOOD,
            isFriendly = true,
            notLate = false,
            respondedQuickly = true
        )

        //아직 거래가 되지 않은 게시글에 리뷰 시도
        val notDoneTradeCase = CreateReviewInput(
            reviewerId = reviewerId,
            postId = notTradePost.id!!,
            mainEvaluation = Evaluation.GOOD,
            isFriendly = true,
            notLate = false,
            respondedQuickly = true
        )

        //when  & then
        assertThrows<ForbiddenException>{
            reviewService.createReview(badUserCase)
        }


        assertThrows<ForbiddenException>{
            reviewService.createReview(notDoneTradeCase)
        }

    }

    @Test
    @DisplayName("리뷰 작성 시 게시글이 존재하지 않을 경우 PostNotFoundException을 반환한다.")
    fun createReviewPostNotFoundExceptionTest(){
        //Given
        val notFoundPostCase = CreateReviewInput(
            reviewerId = UUID.randomUUID(),
            postId = 1L,
            mainEvaluation = Evaluation.GOOD,
            isFriendly = true,
            notLate = true,
            respondedQuickly = true
        )
        //When & Then
        assertThrows<PostNotFoundException>{
            reviewService.createReview(notFoundPostCase)
        }
    }

}