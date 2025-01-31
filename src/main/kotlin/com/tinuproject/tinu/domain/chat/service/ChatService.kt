package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.dto.ChatDTO
import com.tinuproject.tinu.domain.entity.Chat
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service


// 채팅방 관련 엔티티 필요할듯

@Service
class ChatService {

//    private val chatRooms: MutableMap<String, ChatRoom> = mutableMapOf();

//    @PostConstruct
//    fun init() {
//        chatRooms.clear();
//    }

//    // 채팅방 불러오기
//    fun findAllRoom(): List<ChatRoom> {
//        return chatRooms.values.toList().reversed()
//    }
//
//    // 채팅방 하나 불러오기
//    fun findById(roomId: String): ChatRoom? {
//        return chatRooms[roomId]
//    }
//
    // 채팅방 생성
    //ChatRoom은 엔티티
    //추후 엔티티에 create 메서드 추가 필요
    fun createRoom(name: ChatDTO): Chat {
        val chat = Chat.create(name)
        chatRooms[chatRoom.roomId] = chatRoom
        return chatRoom
    }


    //채팅방 참여
    fun enterRoom(name: ChatDTO): Chat {

    }
}