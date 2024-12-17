package com.tinuproject.tinu.web

import jakarta.servlet.http.Cookie

class CookieGenerator {
    companion object{
        fun createCookies(key : String, value : String) : Cookie {
            val cookie = Cookie(key, value)
            cookie.path = "/"
            cookie.isHttpOnly = false
            cookie.secure = false
            cookie.maxAge = 3600

            return cookie
        }
    }
}