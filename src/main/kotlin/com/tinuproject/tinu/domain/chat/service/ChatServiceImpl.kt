package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.dto.response.ChatListResponse
import com.tinuproject.tinu.domain.chat.repository.ChatRepository
import com.tinuproject.tinu.domain.chat.repository.ChatTextRepository
import com.tinuproject.tinu.domain.entity.Chat
import com.tinuproject.tinu.domain.exception.chat.AlreadyExistChatException
import com.tinuproject.tinu.domain.exception.chat.NotAuthorityCreateChatException
import com.tinuproject.tinu.domain.exception.chat.NotCreatedChatException
import com.tinuproject.tinu.domain.exception.common.NotFoundException
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.domain.post.repository.PostRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChatServiceImpl (
    val memberRepository : MemberRepository,
    val chatRepository: ChatRepository,
    val chatTextRepository: ChatTextRepository,
    val postRepository: PostRepository
) : ChatService
{

    @Transactional
    override fun createRoom(userId: UUID, postId : Long) : Long {
        val buyer = memberRepository.findMemberByUserId(userId = userId) ?: throw NotFoundException()
        val post = postRepository.findPostById(postId = postId) ?: throw NotFoundException()
        val chatList = chatRepository.findAllByPostId(postId = postId)

        //condition 1 : 판매 게시글의 작성자가 채팅방 생성 시도 시 필터링
        if(buyer.id != post.author.id) throw NotAuthorityCreateChatException()
        //condition 2 : 채팅방 생성을 시도하는 구매자가 이미 해당 판매 게시글에 대해 채팅방을 생성한 경우 필터링
        if(chatList.any { buyer.id  == it.buyer.id }) throw AlreadyExistChatException()

        val chat = chatRepository.save(Chat(buyer = buyer, seller = post.author, post = post))
        return chat.id ?: throw NotCreatedChatException()
    }

    override fun getList(userId: UUID) : List<ChatListResponse> {
        TODO("Not yet implemented")
    }
}