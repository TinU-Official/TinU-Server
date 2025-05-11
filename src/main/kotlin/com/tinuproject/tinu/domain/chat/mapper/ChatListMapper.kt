package com.tinuproject.tinu.domain.chat.mapper

import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatListGetResponseDto
import com.tinuproject.tinu.domain.chat.service.dto.output.ChatListGetOutputDto
import io.swagger.v3.oas.annotations.media.Schema
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "spring")
@Schema(name = "ChatListMapper", description = "채팅 리스트  Mapper")
interface ChatListMapper {
    fun toChatListGetResponseDto(chatListGetOutputDto: List<ChatListGetOutputDto>): List<ChatListGetResponseDto>
}