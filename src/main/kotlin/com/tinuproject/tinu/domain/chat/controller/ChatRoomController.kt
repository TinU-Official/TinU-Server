package com.tinuproject.tinu.domain.chat.controller

import com.tinuproject.tinu.domain.chat.controller.dto.request.ChatDetailRequest
import com.tinuproject.tinu.domain.chat.controller.dto.request.CreateChatRoomRequest
import com.tinuproject.tinu.domain.chat.controller.dto.request.MarkAsReadRequest
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatDetailResponse
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomInfoResponse
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomListResponse
import com.tinuproject.tinu.domain.chat.controller.dto.response.CreateChatRoomResponse
import com.tinuproject.tinu.domain.chat.enums.ChatDetailDirection
import com.tinuproject.tinu.domain.chat.enums.ChatRoomFilter
import com.tinuproject.tinu.domain.chat.service.ChatRoomDetailService
import com.tinuproject.tinu.domain.chat.service.ChatRoomService
import com.tinuproject.tinu.global.response.ResponseEntityGenerator
import com.tinuproject.tinu.global.response.dto.ResponseDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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

    @GetMapping("/{chatRoomId}")
    @Operation(
        summary = "채팅방 정보 조회",
        description = """
            채팅방 헤더 정보를 조회합니다.
            - 상대방 닉네임, 프로필 이미지, 상품 정보(제목, 가격, 썸네일, 판매 상태) 반환
            - opponentHasLeft: 상대방이 채팅방을 나간 경우 true
        """
    )
    fun getChatRoomInfo(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable chatRoomId: Long
    ): ResponseEntity<ResponseDTO<ChatRoomInfoResponse?>> {
        return ResponseEntityGenerator.onSuccess(
            chatRoomService.getChatRoomInfo(userId, chatRoomId)
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

    @PostMapping
    @Operation(
        summary = "채팅방 생성/입장",
        description = """
            상품에 대해 판매자와 1:1 채팅방을 생성하거나 기존 채팅방에 입장합니다.
            - created: true이면 새로 생성, false이면 기존 채팅방 재입장
            - 자기 자신의 상품에 채팅을 보낼 수 없습니다 (400)
            - 나갔던 채팅방에 재입장하면 이전 메시지 포함 복원됩니다
        """
    )
    fun createChatRoom(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody request: CreateChatRoomRequest
    ): ResponseEntity<ResponseDTO<CreateChatRoomResponse?>> {
        return ResponseEntityGenerator.onSuccess(
            chatRoomService.createOrEnterChatRoom(userId, request),
            201
        )
    }

    @PatchMapping("/{chatRoomId}/leave")
    @Operation(
        summary = "채팅방 나가기",
        description = """
            채팅방을 나갑니다 (soft delete).
            - 나간 사용자: 채팅 목록에서 해당 채팅방 미노출
            - 상대방: opponentHasLeft=true로 표시되며 채팅 전송 불가
            - 이미 나간 채팅방에 재요청 시 정상 응답 (멱등)
        """
    )
    fun leaveChatRoom(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable chatRoomId: Long
    ): ResponseEntity<ResponseDTO<Void?>> {
        chatRoomService.leaveChatRoom(userId, chatRoomId)
        return ResponseEntityGenerator.onSuccess()
    }

    @PatchMapping("/{chatRoomId}/read")
    @Operation(
        summary = "읽음 처리",
        description = """
            채팅방의 lastReadChatId를 업데이트합니다.
            - lastReadChatId: 마지막으로 읽은 ChatText의 id
            - 현재 읽음 커서보다 이전 메시지를 커서로 전달 시 무시 (역행 방지)
        """
    )
    fun markAsRead(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable chatRoomId: Long,
        @RequestBody request: MarkAsReadRequest
    ): ResponseEntity<ResponseDTO<Void?>> {
        chatRoomService.markAsRead(userId, chatRoomId, request)
        return ResponseEntityGenerator.onSuccess()
    }
}
