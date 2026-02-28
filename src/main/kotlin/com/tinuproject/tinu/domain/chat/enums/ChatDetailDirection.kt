package com.tinuproject.tinu.domain.chat.enums

enum class ChatDetailDirection {
    /** 위로 스크롤 - cursor보다 과거 메시지 조회 */
    PREV,

    /**
     * 아래로 스크롤 - cursor보다 최신 메시지 조회.
     * cursor 없을 때 = 0번째(전체 가장 과거)부터 NEXT 조회로 간주 → 최신 size개 반환.
     */
    NEXT
}
