package com.tinuproject.tinu.domain.chat.service

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
//    // 채팅방 생성
//    fun createRoom(name: String): ChatRoom {
//        val chatRoom = ChatRoom.create(name)
//        chatRooms[chatRoom.roomId] = chatRoom
//        return chatRoom
//    }
}