package com.tinuproject.tinu.web


import org.springframework.http.ResponseCookie

class CookieGenerator {
    companion object{
        fun createCookies(key : String, value : String) : String {
            val cookie = ResponseCookie.from(key, value)
                .path("/")
                .maxAge(3600)
                .secure(false)
                .httpOnly(false)
//                .sameSite("None")
                .build()

            return cookie.toString()
        }
    }
}