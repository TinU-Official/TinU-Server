package com.tinuproject.tinu.global.web

import jakarta.servlet.http.HttpServletRequest

interface TokenResolver {
    fun resolve(request : HttpServletRequest) : String
}