package com.tinuproject.tinu.infra.security.oauth.apple

import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse

class DelegatingTokenResponseClient(
    private val appleTokenClient: OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>,
    private val defaultClient: OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>
) : OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    override fun getTokenResponse(request: OAuth2AuthorizationCodeGrantRequest): OAuth2AccessTokenResponse {
        val registrationId = request.clientRegistration.registrationId

        return if (registrationId == "apple") {
            appleTokenClient.getTokenResponse(request)
        } else {
            defaultClient.getTokenResponse(request)
        }
    }
}