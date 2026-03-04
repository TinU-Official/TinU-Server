package com.tinuproject.tinu.global.web

import org.springframework.boot.web.server.Cookie.SameSite
import org.springframework.http.ResponseCookie

class CookieGenerator {
    companion object{

        fun createCookies(key : String, value : String) : String {
            val cookie = ResponseCookie.from(key, value)
                .path("/")
                .maxAge(3600)
                .secure(false)
                .httpOnly(false)
                .sameSite("None")
                .build()

            return cookie.toString()
        }

        fun createCookies(key : String, value : String, sameSite: SameSite, maxAge:Long) : String {
            val cookie = ResponseCookie.from(key, value)
                .path("/")
                .maxAge(maxAge)
                .secure(true)
                .httpOnly(true)
                .sameSite(sameSite.toString())
                .build()

            return cookie.toString()
        }

        fun createCookies(key : String, value : String, path : String, sameSite: SameSite, maxAge:Long) : String {
            val cookie = ResponseCookie.from(key, value)
                .path(path)
                .maxAge(maxAge)
                .secure(true)
                .httpOnly(true)
                .sameSite(sameSite.toString())
                .build()

            return cookie.toString()
        }
    }
}