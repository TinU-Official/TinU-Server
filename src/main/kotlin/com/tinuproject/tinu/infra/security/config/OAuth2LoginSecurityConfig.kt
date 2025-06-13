package com.tinuproject.tinu.infra.security.config

import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.web.SecurityFilterChain

@Configuration
class OAuth2LoginSecurityConfig {

    @Bean
    fun customAuthorizationRequestResolver(
        clientRegistrationRepository: ClientRegistrationRepository
    ): OAuth2AuthorizationRequestResolver {
        val defaultResolver = DefaultOAuth2AuthorizationRequestResolver(
            clientRegistrationRepository,
            OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
        )


        return object : OAuth2AuthorizationRequestResolver {
            override fun resolve(request: HttpServletRequest): OAuth2AuthorizationRequest? {
                val resolved = defaultResolver.resolve(request) ?: return null

                val uri = request.requestURI
                val registrationId = uri.substringAfterLast("/")

                return if (registrationId == "apple") {
                    OAuth2AuthorizationRequest.from(resolved)
                        .additionalParameters { it["response_mode"] = "form_post" }
                        .build()
                } else {
                    resolved
                }
            }

            override fun resolve(request: HttpServletRequest, clientRegistrationId: String): OAuth2AuthorizationRequest? {
                val resolved = defaultResolver.resolve(request, clientRegistrationId) ?: return null

                return if (clientRegistrationId == "apple") {
                    OAuth2AuthorizationRequest.from(resolved)
                        .additionalParameters { it["response_mode"] = "form_post" }
                        .build()
                } else {
                    resolved
                }
            }
        }
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        resolver: OAuth2AuthorizationRequestResolver
    ): SecurityFilterChain {
        http
            .oauth2Login {
                it.authorizationEndpoint { endpoint ->
                    endpoint.authorizationRequestResolver(resolver)
                }
            }
        return http.build()
    }
}