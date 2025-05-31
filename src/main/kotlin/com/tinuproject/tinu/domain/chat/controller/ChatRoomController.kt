package com.tinuproject.tinu.domain.chat.controller

import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomCreateResponseDto
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatListGetResponseDto
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatDetailGetResponseDto
import com.tinuproject.tinu.domain.chat.service.ChatService
import com.tinuproject.tinu.domain.chat.exception.AlreadyExistChatException
import com.tinuproject.tinu.domain.chat.exception.NotAuthorityCreateChatException
import com.tinuproject.tinu.domain.chat.exception.NotFoundChatException
import com.tinuproject.tinu.domain.chat.mapper.ChatMapper
import com.tinuproject.tinu.global.response.ResponseEntityGenerator
import com.tinuproject.tinu.global.response.dto.ResponseDTO
import com.tinuproject.tinu.infra.swagger.annotation.SwaggerExceptionResponses
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/chat")
@Tag(name = "채팅방 api", description = "채팅방 CRD api")
class ChatRoomController (
    private val chatService: ChatService,
    private val mapper: ChatMapper
) {
    //POST METHOD
    @PostMapping("/create")
    @Operation(summary = "채팅방 생성", description = "채팅방을 생성합니다.")
    @SwaggerExceptionResponses(
        exceptions = [
            AlreadyExistChatException::class, // 채팅방이 이미 존재하는 경우(구매자가 동일 판매글에 채팅방 재생성 시도시)
            NotAuthorityCreateChatException::class // 채팅방을 생성할 권한이 없는 경우(판매자 == 판매글 작성자인 경우)
        ]
    )
    fun generateRoomInfo (
        @AuthenticationPrincipal userId : UUID,
        @RequestParam postId : Long
    ): ResponseEntity<ResponseDTO<ChatRoomCreateResponseDto?>> {
        return ResponseEntityGenerator.onSuccess(chatService.createChatRoom(userId, postId))
    }

    //GET METHOD
    @GetMapping("/list")
    @Operation(summary = "채팅방 목록 조회", description = "채팅방 목록을 조회합니다.")
    @SwaggerExceptionResponses()
    fun getChatList (
        @AuthenticationPrincipal userId : UUID,
        @RequestParam sortedType : String
    ): ResponseEntity<ResponseDTO<List<ChatListGetResponseDto>?>> {
        val responses = mapper.toChatListGetResponseDto(chatService.getChatList(userId, sortedType))
        return ResponseEntityGenerator.onSuccess(responses);

    }

    //GET METHOD
    @GetMapping("/{chatId}")
    @Operation(summary = "채팅방 상세 조회", description = "채팅방 상세 정보를 조회합니다.")
    @SwaggerExceptionResponses(
        exceptions = [
            NotFoundChatException::class // 채팅방이 존재하지 않는 경우
        ]
    )
    fun getChatRoomDetail (
        @AuthenticationPrincipal userId : UUID,
        @PathVariable chatId : Long
    ): ResponseEntity<ResponseDTO<ChatDetailGetResponseDto?>> {
        val response = mapper.toChatDetailGetResponseDto(chatService.getChatDetail(userId, chatId))
        return ResponseEntityGenerator.onSuccess(response);
    }

    //DELETE METHOD
//    @DeleteMapping("/chat/{chatId}")
//    @Operation(summary = "채팅방 삭제", description = "채팅방을 삭제합니다.")
//    @SwaggerExceptionResponses()
//    fun deleteChatRoom (
//        @AuthenticationPrincipal userId : UUID,
//        @PathVariable chatId : Long
//    ): ResponseEntity<ResponseDTO> {
//        chatService.deleteChatRoom(userId, chatId)
//        return ResponseEntityGenerator.onSuccess(null);
//    }

}