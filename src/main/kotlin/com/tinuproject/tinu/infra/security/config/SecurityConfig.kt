package com.tinuproject.tinu.infra.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.infra.security.filter.ExceptionHandlerFilter
import com.tinuproject.tinu.infra.security.filter.JwtTokenFilter
import com.tinuproject.tinu.infra.security.filter.SignUpFilter
import com.tinuproject.tinu.infra.security.jwt.JwtUtil
import com.tinuproject.tinu.infra.security.oauth.apple.AppleJwtGenerator
import com.tinuproject.tinu.infra.security.oauth.apple.AppleTokenResponseClient
import com.tinuproject.tinu.infra.security.oauth.handler.OAuthLoginFailureHandler
import com.tinuproject.tinu.infra.security.oauth.handler.OAuthLoginSuccessHandler
import com.tinuproject.tinu.infra.security.oauth.service.CustomOAuth2UserService
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import java.util.*
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.*
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtUtil: JwtUtil,
    private val oauth2LoginSuccessHandler: OAuthLoginSuccessHandler,
    private val oAuthLoginFailureHandler: OAuthLoginFailureHandler,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val memberRepository: MemberRepository,
    @Value("\${web.allowed-path}")
    private val allowedPaths : List<String>,
    private val objectMapper: ObjectMapper,
    private val appleJwtGenerator: AppleJwtGenerator
) {

    @Bean
    fun configure(): WebSecurityCustomizer? {
        return WebSecurityCustomizer { web: WebSecurity ->
            //로그인이 아예 안되어 있어도 괜찮은 api
            //해당 ""에 API 추가시 해당 API는 필터를 거치지 않음.
            web.ignoring().requestMatchers("/swagger-ui/**","/v3/api-docs/**")
        }
    }

    // CORS 설정
    fun corsConfigurationSource() : CorsConfigurationSource {
        return CorsConfigurationSource { request ->
            val config = CorsConfiguration()
            config.allowedHeaders = Collections.singletonList("*")
            config.setAllowedMethods(Collections.singletonList("*"))
            //TODO(배포 전 오리진 추가)
            config.setAllowedOriginPatterns(Collections.singletonList("*")) // 허용할 origin
            config.allowCredentials = true
            config
        }
    }
    @Bean
    fun delegatingTokenResponseClient(
        appleJwtGenerator: AppleJwtGenerator
    ): OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

        val appleTokenClient = AppleTokenResponseClient { appleJwtGenerator.generate() }
        val defaultTokenClient = DefaultAuthorizationCodeTokenResponseClient()

        return object : OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
            override fun getTokenResponse(request: OAuth2AuthorizationCodeGrantRequest): OAuth2AccessTokenResponse {
                val registrationId = request.clientRegistration.registrationId
                val log : Logger = LoggerFactory.getLogger(this::class.java)
                log.info(registrationId)
                return if (registrationId == "apple") {
                    log.info("apple getTokenResponse 실행")
                    appleTokenClient.getTokenResponse(request)
                } else {
                    defaultTokenClient.getTokenResponse(request)
                }
            }
        }
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(httpSecurity: HttpSecurity, memberRepository: MemberRepository,  clientRegistrationRepository: ClientRegistrationRepository): SecurityFilterChain {
        val sessionManagement = httpSecurity.httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }
            //cors 설정
            .cors { corsConfigurer: CorsConfigurer<HttpSecurity?> ->
                corsConfigurer.configurationSource(
                    corsConfigurationSource()
                )
            } // CORS 설정 추가

            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .authorizeHttpRequests(
                Customizer { authorize ->
                    authorize
                        //TODO(배포 전 로그인 되어 있어야만 서비스 이용가능하게 변경)
                        .requestMatchers("/api/token/**").permitAll()
                        .anyRequest().permitAll()//로그인 이후엔 모두 허용
                }
            ).oauth2Login { oauth: OAuth2LoginConfigurer<HttpSecurity?> ->  // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                oauth
//                    .authorizationEndpoint { endpoint ->
//                        endpoint
//                            .authorizationRequestResolver(customAuthorizationRequestResolver(clientRegistrationRepository)) // 이건 필요할 때만
//                    }
                    .userInfoEndpoint { userInfo ->
                        userInfo.userService(customOAuth2UserService) // CustomOAuth2UserService 등록
                    }
                    .tokenEndpoint { token ->
                        token.accessTokenResponseClient(delegatingTokenResponseClient(appleJwtGenerator))
                    }
                    //TODO(로그인이 필요한데 안된 부분이 있으면 넘길 수 있는 것.)- 기본은 (백엔드 도메인)/login
                    //.loginPage("http://localhost:8080/loginpage.html").permitAll()
                    .successHandler(oauth2LoginSuccessHandler) // 로그인 성공 시 핸들러
                    .failureHandler(oAuthLoginFailureHandler) // 로그인 실패 시 핸들러
            }
            .sessionManagement {
                Customizer { session: SessionManagementConfigurer<HttpSecurity?> ->
                    session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                }
            }


            httpSecurity
                .addFilterBefore(JwtTokenFilter(jwtUtil = jwtUtil, excludeUrls =allowedPaths), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(ExceptionHandlerFilter(objectMapper), JwtTokenFilter::class.java)
                .addFilterAfter(SignUpFilter(jwtUtil=jwtUtil, excludeUrls =  allowedPaths), JwtTokenFilter::class.java)


        return httpSecurity.build()

    }

    @Bean
    fun appleTokenResponseClient(): OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
        return AppleTokenResponseClient {
            appleJwtGenerator.generate()
        }
    }

    private fun customAuthorizationRequestResolver(
        clientRegistrationRepository: ClientRegistrationRepository
    ): OAuth2AuthorizationRequestResolver {
        val defaultResolver = DefaultOAuth2AuthorizationRequestResolver(
            clientRegistrationRepository,
            OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
        )

        val log: Logger = LoggerFactory.getLogger(this::class.java)

        return object : OAuth2AuthorizationRequestResolver {
            override fun resolve(request: HttpServletRequest): OAuth2AuthorizationRequest? {
                val resolved = defaultResolver.resolve(request) ?: return null

                val registrationId = request.getParameter("registrationId") ?: extractRegistrationId(request)

                log.info("resolve(1) 실행 - registrationId: {}", registrationId)

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

                log.info("resolve(2) 실행 - clientRegistrationId: {}", clientRegistrationId)

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

    private fun extractRegistrationId(request: HttpServletRequest): String {
        val uri = request.requestURI
        return uri.substringAfterLast("/")
    }


}