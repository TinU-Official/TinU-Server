package com.tinuproject.tinu.global.exception.base

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatusCode : Int,
    val stateCode : String?,
    val message :String
) {
    //TOKEN 관련
    TOKEN_MISSING(httpStatusCode = 401, stateCode = "TOKEN_MISSING", message = "토큰이 존재하지 않습니다."),
    TOKEN_INVALIDED(httpStatusCode = 401, stateCode = "TOKEN_INVALIDED", message = "토큰이 유효하지 않습니다."),
    TOKEN_EXPIRED(httpStatusCode = 401, stateCode = "TOKEN_EXPIRED", message = "토큰이 만료되었습니다."),
    TOKEN_UNRECOGNIZED(httpStatusCode = 401, stateCode = "TOKEN_UNRECOGNIZED", message = "토큰을 인식할 수 없습니다."),

    //AUTH 관련
    NEED_LOGIN(httpStatusCode = 401, stateCode = "NEED_LOGIN", message = "로그인을 진행해주세요."),
    NEED_REISSUE_TOKEN(httpStatusCode = 401, stateCode =  "NEED_REFRESH_TOKEN", message = "토큰 재발행을 진행해주세요."),

    //멤버 관련
    MEMBER_EXIST_EMAIL(httpStatusCode = 409, stateCode="ALREADY_EXIST_EMAIL", message = "이미 사용중인 이메일입니다."),
    MEMBER_EXIST_NICKNAME(httpStatusCode = 409, stateCode = "ALREADY_EXIST_NICKNAME", message = "이미 사용중인 닉네임입니다."),
    MEMBER_NOT_EXIST(httpStatusCode = 404, stateCode = "NOT_FOUND_MEMBER", message = "요청하신 이용자는 없는 이용자입니다."),
    MEMBER_EXIST(httpStatusCode = 409, stateCode = "ALREADY_EXIST_MEMBER", message = "이미 회원가입이 진행된 계정입니다."),
    MEMBER_NEED_REGIST(httpStatusCode = 403, stateCode = "MEMBER_NEED_REGIST", message = "회원가입이 완료되지 않은 사용자입니다."),

    //회원가입 - 이메일 인증
    UNIVERSITY_NOT_EXIST_DOMAIN(httpStatusCode = 400, stateCode = "NOT_EXIST_DOMAIN", message = "현재 서비스를 지원하는 학교가 아닌 것 같습니다."),
    NOT_EXIST_CODE(httpStatusCode = 400, stateCode = "NOT_EXIST_CODE", message = "인증 코드를 재요청해주세요."),
    NOT_MATCH_CODE(httpStatusCode = 400, stateCode = "NOT_MATCH_CODE", message = "인증 코드가 일치하지 않습니다."),
    NEED_EMAIL_AUTH(httpStatusCode = 400, stateCode = "NEED_EMAIL_AUTH", message = "이메일 인증이 필요합니다."),

    //판매글
    POST_NOT_FOUND(httpStatusCode = 404, stateCode = "POST_NOT_FOUND", message = "게시글이 존재하지 않습니다."),
    POST_HIDDEN(httpStatusCode = 403, stateCode = "POST_HIDDEN", message = "숨김 처리된 게시글입니다."),
    CATEGORY_NOT_FOUND(httpStatusCode = 404, stateCode = "CATEGORY_NOT_FOUND", message = "카테고리가 존재하지 않습니다."),
    HASHTAG_NOT_FOUND(httpStatusCode = 404, stateCode = "HASHTAG_NOT_FOUND", message = "해시태그가 존재하지 않습니다."),
    AUTHOR_NOT_MATCH(httpStatusCode = 403, stateCode = "AUTHOR_NOT_MATCH", message = "작성자가 일치하지 않습니다."),

    //스크랩
    SCRAP_NOT_FOUND(httpStatusCode = 404, stateCode = "SCRAP_NOT_FOUND", message = "스크랩한 게시글이 존재하지 않습니다."),
    SCRAP_ALREADY_EXIST(httpStatusCode = 409, stateCode = "SCRAP_ALREADY_EXIST", message = "이미 스크랩한 게시글입니다."),

    //커스텀 필터
    FILTER_NOT_EXIST(httpStatusCode = 404, stateCode = "NOT_EXIST_FILTER", message = "요청하신 커스텀 필터를 찾을 수 없습니다."),

    //S3 관련
    UPLOAD_SIZE_OUT_OF_RANGE(httpStatusCode = 400, stateCode = "UPLOAD_SIZE_OUT_OF_RANGE", message = "업로드 요청 범위를 벗어났습니다."),
    NOT_ALLOWED_EXTENSION(httpStatusCode = 400, stateCode = "NOT_ALLOWED_EXTENSION", message = "제공되지 않는 확장자 파일입니다."),
    FILE_LENGTH_OUT_OF_RANGE(httpStatusCode = 400, stateCode = "FILE_LENGTH_OUT_OF_RANGE", message = "파일 크기 범위를 벗어났습니다."),
    NO_SUCH_KEY(httpStatusCode = 400, stateCode = "NO_SUCH_KEY", message = "키가 존재하지 않습니다."),
    INVALID_ETAG(httpStatusCode = 400, stateCode = "INVALID_ETAG", message = "ETag가 일치하지 않습니다."),


    //리뷰 관련
    REVIEW_ALREADY_EXIST(httpStatusCode = 409, stateCode = "REVIEW_ALREADY_EXIST", message = "이미 리뷰를 작성하셨습니다."),

    //채팅 관련
    CHAT_ROOM_NOT_FOUND(httpStatusCode = 404, stateCode = "CHAT_ROOM_NOT_FOUND", message = "채팅방이 존재하지 않습니다."),
    CHAT_ROOM_SELF_CHAT(httpStatusCode = 400, stateCode = "CHAT_ROOM_SELF_CHAT", message = "자기 자신에게 채팅을 보낼 수 없습니다."),
    CHAT_ROOM_MEMBER_NOT_FOUND(httpStatusCode = 404, stateCode = "CHAT_ROOM_MEMBER_NOT_FOUND", message = "채팅방 멤버 정보가 존재하지 않습니다."),
    CHAT_READ_INVALID_CURSOR(httpStatusCode = 400, stateCode = "CHAT_READ_INVALID_CURSOR", message = "유효하지 않은 읽음 커서입니다."),

    //전역적 사용
    UNIVERSITY_NOT_MATCH(httpStatusCode = 403, stateCode = "UNIVERSITY_NOT_MATCH", message = "대학이 일치하지 않습니다."),
    NOT_FOUND(httpStatusCode = 404, stateCode = "NOT_FOUND", message = "없는 페이지입니다."),
    FORBIDDEN(httpStatusCode = 403, stateCode = "FORBIDDEN", message = "요청에 대한 권한이 없습니다."),
    BAD_REQUEST(httpStatusCode = 400, stateCode = "BAD_REQUEST", message = "잘못된 요청입니다.")

}