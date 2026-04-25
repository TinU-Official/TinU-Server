package com.tinuproject.tinu.infra.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.infra.security.filter.JwtTokenFilter
import com.tinuproject.tinu.infra.security.handler.CustomAccessDeniedHandler
import com.tinuproject.tinu.infra.security.handler.CustomAuthenticationEntryPoint
import com.tinuproject.tinu.infra.security.jwt.AppleJwtGenerator
import com.tinuproject.tinu.infra.security.jwt.JwtUtil
import com.tinuproject.tinu.infra.security.oauth.handler.OAuthLoginFailureHandler
import com.tinuproject.tinu.infra.security.oauth.handler.OAuthLoginSuccessHandler
import com.tinuproject.tinu.infra.security.oauth.resolver.CustomAuthorizationRequestResolver
import com.tinuproject.tinu.infra.security.oauth.service.CustomOAuth2UserService
import com.tinuproject.tinu.infra.security.oauth.tokenresponseclient.AppleTokenResponseClient
import com.tinuproject.tinu.infra.security.oauth.tokenresponseclient.CustomTokenResponseClient
import org.springframework.beans.factory.annotation.Autowired
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
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtUtil: JwtUtil,
    private val oauth2LoginSuccessHandler: OAuthLoginSuccessHandler,
    private val oAuthLoginFailureHandler: OAuthLoginFailureHandler,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val customAuthenticationEntryPoint: AuthenticationEntryPoint,
    private val customAccessDeniedHandler: AccessDeniedHandler
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
    @Throws(Exception::class)
    fun filterChain(httpSecurity: HttpSecurity, memberRepository: MemberRepository, clientRegistrationRepository: ClientRegistrationRepository,
                    appleJwtGenerator: AppleJwtGenerator
    ): SecurityFilterChain {
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
                        .requestMatchers("/test/permit-all").permitAll()
                        .requestMatchers("/test/authenticated").fullyAuthenticated()
                        .requestMatchers("/test/user").hasRole("USER")
                        .requestMatchers(
                            "/login",
                            "/favicon.ico",
                            "/api/token/refresh",
                            "/tinu/"
                        ).permitAll()
                        .requestMatchers("/api/user/**").hasRole("USER")
                        .requestMatchers("/api/user").hasRole("USER")
                        .anyRequest().authenticated()//로그인 이후엔 모두 허용
                }
            )
            .sessionManagement {
                Customizer { session: SessionManagementConfigurer<HttpSecurity?> ->
                    session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                }
            }
            .exceptionHandling {
                // 인증 실패 (401) -> CustomAuthenticationEntryPoint
                it.authenticationEntryPoint(customAuthenticationEntryPoint)

                // 인가 실패 (403) -> CustomAccessDeniedHandler
                it.accessDeniedHandler(customAccessDeniedHandler)
            }
            .oauth2Login { oauth ->
                oauth
                    .authorizationEndpoint { endpoint ->
                        endpoint.authorizationRequestResolver(
                            CustomAuthorizationRequestResolver(
                                clientRegisterRepository = clientRegistrationRepository
                            )
                        )
                    }
                    .userInfoEndpoint { userInfo ->
                        userInfo.userService(customOAuth2UserService)
                    }
                    .tokenEndpoint { token ->
                        token.accessTokenResponseClient(
                            CustomTokenResponseClient(
                                appleTokenResponseClient = AppleTokenResponseClient {
                                    appleJwtGenerator.generate()
                                },
                                defaultClient = DefaultAuthorizationCodeTokenResponseClient()
                            )
                        )
                    }
                    .successHandler(oauth2LoginSuccessHandler)
                    .failureHandler(oAuthLoginFailureHandler)

            }

            httpSecurity
                .addFilterBefore(JwtTokenFilter(jwtUtil = jwtUtil), UsernamePasswordAuthenticationFilter::class.java)

        return httpSecurity.build()

    }
}