package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.dto.response.ChatListResponse
import com.tinuproject.tinu.domain.chat.dto.response.CreateChatResponse
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

    /*
    TODO. condition 2 관련 성능개선점
     post_id로 연관된 Chat 객체를 모두 찾은 다음, 해당 객체에서 buyer_id 속성이 일치하는 지를 판단하여 condition 2를 처리하는데,
     쿼리를 직접 작성할 수 있다면 그렇게 할 필요 없이 조건에 buyer_id와 post_id를 달아 조회하여 null 여부를 가지고 처리하고 싶다.
     */

    @Transactional
    override fun createRoom(userId: UUID, postId : Long) : CreateChatResponse {
        val buyer = memberRepository.findMemberByUserId(userId = userId) ?: throw NotFoundException()
        val post = postRepository.findPostById(postId = postId) ?: throw NotFoundException()
        val chatList = chatRepository.findAllByPostId(postId = postId)

        //condition 1 : 판매 게시글의 작성자가 채팅방 생성 시도 시 필터링
        if(buyer.id != post.author.id) throw NotAuthorityCreateChatException()
        //condition 2 : 채팅방 생성을 시도하는 구매자가 이미 해당 판매 게시글에 대해 채팅방을 생성한 경우 필터링
        if(chatList.any { buyer.id  == it.buyer.id }) throw AlreadyExistChatException()

        val chat = chatRepository.save(Chat(buyer = buyer, seller = post.author, post = post))

        //condition 3 : 채팅방이 정상적으로 생성되지 않아 id를 가져올 수 없음을 필터링
        if(chat.id == null) throw NotCreatedChatException()

        return CreateChatResponse(chatId = chat.id)
    }

    override fun getList(userId: UUID) : List<ChatListResponse> {
        TODO("Not yet implemented")
    }
}