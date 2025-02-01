package com.tinuproject.tinu.domain.token.refreshtoken.service

import com.tinuproject.tinu.domain.token.Tokens
import org.springframework.stereotype.Service


interface RefreshTokenService {
    fun reissueAccessTokenByRefreshToken(refreshToken : String) : Tokens

    fun deleteRefreshToken(refreshToken: String)
}