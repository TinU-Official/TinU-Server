package com.tinuproject.tinu.domain.chat.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.chat.dto.response.CreateChatResponse
import com.tinuproject.tinu.domain.chat.dto.response.GetListChatResponse
import com.tinuproject.tinu.domain.chat.service.ChatService
import com.tinuproject.tinu.domain.exception.chat.AlreadyExistChatException
import com.tinuproject.tinu.domain.exception.chat.NotAuthorityCreateChatException
import com.tinuproject.tinu.swagger.annotation.SwaggerExceptionResponses
import com.tinuproject.tinu.web.ResponseEntityGenerator
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/chat")
@Tag(name = "채팅방 api", description = "웹소켓을 사용하지 않는 채팅방 CRUD api 입니다.")
class ChatRoomController (
    private val chatService: ChatService
) {

    //POST METHOD(/api/chat/create) : 채팅방 생성
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
        @RequestParam postId : Long // TODO. null로 들어오는 것에 대한 예외 처리 필요
    ): ResponseEntity<ResponseDTO<CreateChatResponse?>> {
        return ResponseEntityGenerator.onSuccess(chatService.createRoom(userId, postId))
    }

    //GET METHOD(/api/chat/list) : 채팅방 목록 조회
    @GetMapping("/list")
    fun getChatList (
        @AuthenticationPrincipal userId : UUID,
        @RequestParam sortedType : String // TODO. null로 들어오는 것에 대한 예외 처리 필요
    ): ResponseEntity<ResponseDTO<List<GetListChatResponse>?>> {
        return ResponseEntityGenerator.onSuccess(chatService.getList(userId, sortedType));

    }

    //GET METHOD(/api/chat/room/{roomId}/detail) : 채팅방 상세 조회
//    @GetMapping("/room/{roomId}/detail")
//    fun getChatDetail (
//        @AuthenticationPrincipal userId : UUID,
//        @PathVariable roomId : Long,
//    ): ResponseEntity<ResponseDTO<NullResponse?>> {
//        return ResponseEntityGenerator.onSuccess(NullResponse()
//    )
//
}