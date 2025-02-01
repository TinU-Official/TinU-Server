package com.tinuproject.tinu.domain.chat.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.chat.dto.ChatDTO
import com.tinuproject.tinu.domain.chat.service.ChatService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

class ChatRoomController (
    private val chatService: ChatService
) {

    // 채팅방 리스트 조회
//    @GetMapping("/roomList")


    // 채팅방 생성
    //모델 객체 사용 이유?
    @GetMapping("/room")
    fun generateRoom(
        httpServletResponse: HttpServletResponse,
        @CookieValue(name = "Authorization") userId : Long?,
        @RequestParam roomId : Long?,
        model: Model
    ): ResponseEntity<ResponseDTO> {

        val responseDTO = ResponseDTO(
            isSuccess = true,
            stateCode = 200,
            result = chatService.createRoom(ChatDTO(userId = userId, roomId = roomId))
        )

        return ResponseEntity.ok().body(responseDTO)
    }


    //채팅방 입장 <- 근데 이거 필요한가? 논의 필요
    //쿠키 검증? 로그인 관련 pr 머지 이후 해결
    //모델 객체 사용 이유?
    @GetMapping("/room/{roomId}")
    fun enterRoom(
        httpServletResponse: HttpServletResponse,
        @CookieValue(name = "Authorization") userId : Long?,
        @PathVariable roomId : Long?,
        model: Model
    ): ResponseEntity<ResponseDTO> {

        val responseDTO = ResponseDTO(
            isSuccess = true,
            stateCode = 200,
            result = chatService.enterRoom(ChatDTO(userId = userId, roomId = roomId))
        )

        return ResponseEntity.ok().body(responseDTO)

    }
}