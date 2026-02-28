package com.tinuproject.tinu.domain.chat.controller.dto.request

import com.tinuproject.tinu.domain.chat.enums.ChatDetailDirection

data class ChatDetailRequest(
    /**
     * 조회 시작점 ChatText.id.
     * null이면 cursor 없는 초기 진입: 0번째(전체 가장 과거)부터 NEXT로 간주 → 최신 size개 반환.
     */
    val cursor: Long?,

    /**
     * 조회 방향.
     * PREV: cursor보다 과거 메시지(위로 스크롤)
     * NEXT: cursor보다 최신 메시지(아래로 스크롤)
     * cursor 없을 때는 무시되며 NEXT로 동작.
     */
    val direction: ChatDetailDirection = ChatDetailDirection.PREV,

    /** 페이지 크기 (기본 20, 1~100) */
    val size: Int = 20
)
