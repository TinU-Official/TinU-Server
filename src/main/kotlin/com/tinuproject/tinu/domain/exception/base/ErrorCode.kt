package com.tinuproject.tinu.domain.exception.base

enum class ErrorCode(
    val httpStatusCode : Int,
    val stateCode : String?,
    val message :String
) {
    TOKEN_MISSING(httpStatusCode = 401, stateCode = "TOKEN_MISSING", message = "토큰이 존재하지 않습니다."),
    TOKEN_INVALIDED(httpStatusCode = 401, stateCode = "TOKEN_INVALIDED", message = "토큰이 유효하지 않습니다."),
    TOKEN_EXPIRED(httpStatusCode = 401, stateCode = "TOKEN_EXPIRED", message = "토큰이 만료되었습니다."),


    //멤버 관련
    MEMBER_EXIST_EMAIL(httpStatusCode = 400, stateCode="ALREADY_EXIST_EMAIL", message = "이미 사용중인 이메일입니다."),
    MEMBER_EXIST_NICKNAME(httpStatusCode = 400, stateCode = "ALREADY_EXIST_NICKNAME", message = "이미 사용중인 닉네임입니다."),
    MEMBER_NOT_EXIST(httpStatusCode = 400, stateCode = "NOT_FOUND_MEMBER", message = "요청하신 이용자는 없는 이용자입니다."),
    MEMBER_EXIST(httpStatusCode = 400, stateCode = "ALREADY_EXIST_MEMBER", message = "이미 회원가입이 진행된 계정입니다."),

    //회원가입 - 이메일 인증
    UNIVERSITY_NOT_EXIST_DOMAIN(httpStatusCode = 400, stateCode = "NOT_EXIST_DOMAIN", message = "현재 서비스를 지원하는 학교가 아닌 것 같습니다."),
    NOT_EXIST_CODE(httpStatusCode = 400, stateCode = "NOT_EXIST_CODE", message = "인증 코드를 재요청해주세요."),
    NOT_MATCH_CODE(httpStatusCode = 400, stateCode = "NOT_MATCH_CODE", message = "인증 코드가 일치하지 않습니다."),
    NEED_EMAIL_AUTH(httpStatusCode = 400, stateCode = "NEED_EMAIL_AUTH", message = "이메일 인증이 필요합니다."),


    //전역적 사용
    NOT_FOUND(httpStatusCode = 404, stateCode = "NOT_FOUND", message = "없는 페이지입니다."),
    UNAUTHORIZED_ACCESS(httpStatusCode = 400, stateCode = "NOT_ACCESS", message = "잘못된 접근이 감지되었습니다.");
}