package com.tinuproject.tinu.domain.chat.controller

import com.tinuproject.tinu.domain.chat.controller.dto.request.ChatMessageRequestDto
import com.tinuproject.tinu.domain.chat.controller.dto.request.ChatTypingStatusRequestDto
import com.tinuproject.tinu.domain.chat.mapper.ChatMapper
import com.tinuproject.tinu.domain.chat.service.ChatMessageService
import com.tinuproject.tinu.domain.chat.service.ChatService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.tags.Tag

@RestController
@RequestMapping("/api/chat")
@Tag(name = "채팅 메시지 api", description = "웹소켓을 사용하는 채팅 메시지 전송 api 입니다.")
class ChatMessageController (
    private val chatMessageService: ChatMessageService,
    private val mapper: ChatMapper
) {
    //SEND MESSAGE
    @MessageMapping("/message/{roomId}")
    @SendTo("/room/{roomId}")
    @Operation(summary = "채팅방 메시지 전송", description = "채팅방에 메시지를 전송합니다.")
    fun sendMessage(chatMessageRequestDto: ChatMessageRequestDto) {
        chatMessageService.sendMessage(mapper.toChatMessageInputDto(chatMessageRequestDto))
    }

    //SEND TYPING STATUS
    @MessageMapping("/status/{roomId}")
    @SendTo("/status/{roomId}")
    @Operation(summary = "채팅방 작성 상태 전송", description = "채팅방에 작성 상태를 전송합니다.")
    fun sendTypingStatus(chatTypingStatusRequestDto: ChatTypingStatusRequestDto) {
        chatMessageService.sendTypingStatus(mapper.toChatTypingStatusInputDto(chatTypingStatusRequestDto))
    }
}