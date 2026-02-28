package com.tinuproject.tinu.domain.chat.controller

import com.tinuproject.tinu.domain.chat.controller.dto.request.ChatDetailRequest
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatDetailResponse
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomListResponse
import com.tinuproject.tinu.domain.chat.enums.ChatDetailDirection
import com.tinuproject.tinu.domain.chat.enums.ChatRoomFilter
import com.tinuproject.tinu.domain.chat.exception.ChatRoomNotFoundException
import com.tinuproject.tinu.domain.chat.service.ChatRoomDetailService
import com.tinuproject.tinu.domain.chat.service.ChatRoomService
import com.tinuproject.tinu.global.exception.ForbiddenException
import com.tinuproject.tinu.global.response.ResponseEntityGenerator
import com.tinuproject.tinu.global.response.dto.ResponseDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat API", description = "채팅 관련 API(WebSocket 제외)")
class ChatRoomController(
    private val chatRoomService: ChatRoomService,
    private val chatRoomDetailService: ChatRoomDetailService
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

    @GetMapping("/{chatRoomId}/messages")
    @Operation(
        summary = "채팅 상세 조회",
        description = """
            채팅방의 메시지 목록을 커서 기반 페이징으로 조회합니다.
            - cursor: ChatText.id (null이면 초기 진입 → 최신 size개 반환)
            - direction: PREV(위로 스크롤, 과거) / NEXT(아래로 스크롤, 최신). 기본값 PREV
            - cursor 없을 때는 0번째부터 NEXT 방향으로 간주 (최신 size개)
            - 응답: 날짜별 그룹핑(KST), isMine 구분, prevCursor/nextCursor, hasPrev/hasNext
        """
    )
    fun getChatDetail(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable chatRoomId: Long,
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "PREV") direction: ChatDetailDirection,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ResponseDTO<ChatDetailResponse?>> {
        val request = ChatDetailRequest(cursor = cursor, direction = direction, size = size)
        return ResponseEntityGenerator.onSuccess(
            chatRoomDetailService.getChatDetail(userId, chatRoomId, request)
        )
    }
}
