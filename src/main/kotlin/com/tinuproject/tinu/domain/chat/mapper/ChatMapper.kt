package com.tinuproject.tinu.domain.chat.mapper

import com.tinuproject.tinu.domain.chat.controller.dto.request.ChatMessageRequestDto
import com.tinuproject.tinu.domain.chat.controller.dto.request.ChatTypingStatusRequestDto
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatListGetResponseDto
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatDetailGetResponseDto
import com.tinuproject.tinu.domain.chat.service.dto.input.ChatMessageInputDto
import com.tinuproject.tinu.domain.chat.service.dto.input.ChatTypingStatusInputDto
import com.tinuproject.tinu.domain.chat.service.dto.output.ChatListGetOutputDto
import com.tinuproject.tinu.domain.chat.service.dto.output.ChatDetailGetOutputDto
import io.swagger.v3.oas.annotations.media.Schema
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
@Schema(name = "ChatMapper", description = "채팅 리스트  Mapper")
interface ChatMapper {
    // ChatListGetOutputDto -> ChatListGetResponseDto 변환
    fun toChatListGetResponseDto(chatListGetOutputDto: List<ChatListGetOutputDto>): List<ChatListGetResponseDto>

    // ChatDetailGetOutputDto -> ChatDetailGetResponseDto 변환
    fun toChatDetailGetResponseDto(chatDetailGetOutputDto: ChatDetailGetOutputDto): ChatDetailGetResponseDto

    // ChatMessageRequestDto -> ChatMessageInputDto 변환
    fun toChatMessageInputDto(chatMessageRequestDto: ChatMessageRequestDto): ChatMessageInputDto

    // ChatTypingStatusRequestDto -> ChatTypingStatusInputDto 변환
    fun toChatTypingStatusInputDto(chatTypingStatusRequestDto: ChatTypingStatusRequestDto): ChatTypingStatusInputDto
}