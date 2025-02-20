package com.tinuproject.tinu.domain.chat.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.chat.dto.ChatDTO
import com.tinuproject.tinu.domain.chat.service.ChatService
import com.tinuproject.tinu.web.ResponseEntityGenerator
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class ChatRoomController (
    private val chatService: ChatService
) {

    // 채팅방 생성(채팅방 id 생성)
    @GetMapping("/create")
    fun generateRoom(
        httpServletResponse: HttpServletResponse,
        @CookieValue(name = "Authorization") userId : Long?,
        @RequestParam roomId : Long?
    ): ResponseEntity<ResponseDTO> {

        return ResponseEntityGenerator.onSuccess(chatService.createRoom(ChatDTO(userId = userId, roomId = roomId)));

    }


    // 채팅방 리스트 조회 :
    @GetMapping("/list")
    fun enterRoom(
        httpServletResponse: HttpServletResponse,
        @CookieValue(name = "Authorization") userId : Long?
    ): ResponseEntity<ResponseDTO> {

        return ResponseEntityGenerator.onSuccess(chatService.getList(userId));

    }
}