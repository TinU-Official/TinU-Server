package com.tinuproject.tinu.infra.security.oauth.apple

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse


class AppleTokenResponseClient(
    private val jwtGenerator: () -> String
) : OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private val restTemplate = RestTemplate()

    override fun getTokenResponse(request: OAuth2AuthorizationCodeGrantRequest): OAuth2AccessTokenResponse {
        val clientRegistration = request.clientRegistration
        val redirectUri = request.authorizationExchange.authorizationRequest.redirectUri
        val code = request.authorizationExchange.authorizationResponse.code

        val formData = LinkedMultiValueMap<String, String>().apply {
            add("client_id", clientRegistration.clientId)
            add("client_secret", jwtGenerator())
            add("code", code)
            add("grant_type", "authorization_code")
            add("redirect_uri", redirectUri)
        }

        val log :Logger = LoggerFactory.getLogger(this::class.java)

        log.info(formData["client_secret"].toString())
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val entity = HttpEntity(formData, headers)

        val response = restTemplate.exchange(
            clientRegistration.providerDetails.tokenUri,
            HttpMethod.POST,
            entity,
            object : ParameterizedTypeReference<Map<String, Any>>() {}
        ).body ?: throw IllegalStateException("Apple token response is null")

        val accessToken = response["access_token"] as String
        val refreshToken = response["refresh_token"] as? String
        val expiresIn = (response["expires_in"] as Number).toLong()

        return OAuth2AccessTokenResponse.withToken(accessToken)
            .tokenType(OAuth2AccessToken.TokenType.BEARER)
            .expiresIn(expiresIn)
            .refreshToken(refreshToken)
            .scopes(clientRegistration.scopes)
            .additionalParameters(response)
            .build()
    }

}