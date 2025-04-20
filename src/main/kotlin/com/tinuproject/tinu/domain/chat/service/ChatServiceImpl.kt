package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.dto.response.GetListChatResponse
import com.tinuproject.tinu.domain.chat.dto.response.CreateChatResponse
import com.tinuproject.tinu.domain.chat.repository.ChatRepository
import com.tinuproject.tinu.domain.chat.repository.ChatTextRepository
import com.tinuproject.tinu.domain.entity.Chat
import com.tinuproject.tinu.domain.exception.chat.AlreadyExistChatException
import com.tinuproject.tinu.domain.exception.chat.NotAuthorityCreateChatException
import com.tinuproject.tinu.domain.exception.chat.NotCreatedChatException
import com.tinuproject.tinu.domain.exception.common.NotFoundException
import com.tinuproject.tinu.domain.exception.mail.NotExistMemberException
import com.tinuproject.tinu.domain.exception.post.PostNotFoundException
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.domain.post.repository.PostRepository
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
    val log : Logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun createRoom(userId: UUID, postId : Long) : CreateChatResponse {
        val buyer = memberRepository.findMemberByUserId(userId = userId) ?: throw NotExistMemberException();
        val post = postRepository.findPostById(id = postId) ?: throw PostNotFoundException()
        val chatList = chatRepository.findByPostIdAndBuyerId(postId = postId, buyerId = buyer.id!!)

        //condition 1 : 판매 게시글의 작성자가 채팅방 생성 시도 시 필터링
        if(buyer.id != post.author.id) throw NotAuthorityCreateChatException()
        //condition 2 : 채팅방 생성을 시도하는 구매자가 이미 해당 판매 게시글에 대해 채팅방을 생성한 경우 필터링
        if(chatList != null) throw AlreadyExistChatException()

        val chat = chatRepository.save(Chat(buyer = buyer, seller = post.author, post = post))
        return CreateChatResponse(chatId = chat.id)
    }

    override fun getList(userId : UUID, sortedType : String) : List<GetListChatResponse> {
        val member = memberRepository.findMemberByUserId(userId = userId) ?: throw NotExistMemberException()
        return chatRepository.findChatListByUserIdAndType(member.id!!, sortedType)
    }
}