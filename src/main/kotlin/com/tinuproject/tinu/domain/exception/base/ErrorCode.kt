package com.tinuproject.tinu.domain.exception.base

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatusCode : Int,
    val stateCode : String?,
    val message :String
) {
    TOKEN_MISSING(httpStatusCode = 401, stateCode = "TOKEN_MISSING", message = "토큰이 존재하지 않습니다."),
    TOKEN_INVALIDED(httpStatusCode = 401, stateCode = "TOKEN_INVALIDED", message = "토큰이 유효하지 않습니다."),
    TOKEN_EXPIRED(httpStatusCode = 401, stateCode = "TOKEN_EXPIRED", message = "토큰이 만료되었습니다."),

    //멤버 관련
    MEMBER_EXIST_EMAIL(httpStatusCode = 409, stateCode="ALREADY_EXIST_EMAIL", message = "이미 사용중인 이메일입니다."),
    MEMBER_EXIST_NICKNAME(httpStatusCode = 409, stateCode = "ALREADY_EXIST_NICKNAME", message = "이미 사용중인 닉네임입니다."),
    MEMBER_NOT_EXIST(httpStatusCode = 404, stateCode = "NOT_FOUND_MEMBER", message = "요청하신 이용자는 없는 이용자입니다."),
    MEMBER_EXIST(httpStatusCode = 409, stateCode = "ALREADY_EXIST_MEMBER", message = "이미 회원가입이 진행된 계정입니다."),
    MEMBER_NEED_REGIST(httpStatusCode = HttpStatus.UNAUTHORIZED.value(), stateCode = "MEMBER_NEED_REGIST", message = "회원가입이 완료되지 않은 사용자입니다."),

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

    //채팅 관련
    ALREADY_EXIST_CHAT(httpStatusCode = 400, stateCode = "ALREADY_EXIST_CHAT", message = "채팅방이 이미 존재합니다. 새로 생성할 수 없습니다."),
    NOT_AUTHORITY_FOR_CREATE_CHAT(httpStatusCode = 400, stateCode = "NOT_AUTHORITY_FOR_CREATE_CHAT", message = "채팅방을 만들 권한이 없습니다."),
    NOT_CREATED_CHAT_ID(httpStatusCode = 400, stateCode = "NOT_CREATED_CHAT_ID", message = "채팅방 생성에 실패했습니다. 다시 시도해주세요."),

    //전역적 사용
    UNIVERSITY_NOT_MATCH(httpStatusCode = 403, stateCode = "UNIVERSITY_NOT_MATCH", message = "대학이 일치하지 않습니다."),
    NOT_FOUND(httpStatusCode = 404, stateCode = "NOT_FOUND", message = "없는 페이지입니다."),
    UNAUTHORIZED_ACCESS(httpStatusCode = 403, stateCode = "FORBIDDEN", message = "요청에 대한 권한이 없습니다.");
}