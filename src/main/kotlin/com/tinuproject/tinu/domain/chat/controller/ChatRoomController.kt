package com.tinuproject.tinu.domain.chat.controller

import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomListResponse
import com.tinuproject.tinu.domain.chat.enums.ChatRoomFilter
import com.tinuproject.tinu.domain.chat.service.ChatRoomService
import com.tinuproject.tinu.global.response.ResponseEntityGenerator
import com.tinuproject.tinu.global.response.dto.ResponseDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat API", description = "채팅 관련 API(WebSocket 제외)")
class ChatRoomController(
    private val chatRoomService: ChatRoomService
) {

    @GetMapping
    @Operation(
        summary = "채팅 목록 조회",
        description = """
            내가 참여한 채팅방 목록을 조회합니다.
            - filter: ALL(전체), PURCHASE(구매), SALE(판매)
            - cursorId: 'chatRoomId_order' 형식의 커서 (예: '123_45'), 첫 페이지는 생략
            - 정렬: 마지막 메시지 시각 최신순
        """
    )
    fun getChatRoomList(
        @AuthenticationPrincipal userId: UUID,
        @RequestParam(defaultValue = "ALL") filter: ChatRoomFilter,
        @RequestParam(required = false) cursorId: String?,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ResponseDTO<ChatRoomListResponse?>> {
        return ResponseEntityGenerator.onSuccess(
            chatRoomService.getChatRoomList(userId, filter, cursorId, size)
        )
    }
}
