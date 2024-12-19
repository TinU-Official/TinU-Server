package com.tinuproject.tinu.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.tinuproject.tinu.security.filter.ExceptionHandlerFilter
import com.tinuproject.tinu.security.filter.JwtTokenFilter
import com.tinuproject.tinu.security.jwt.JwtUtil
import com.tinuproject.tinu.security.oauth2.handler.OAuthLoginFailureHandler
import com.tinuproject.tinu.security.oauth2.handler.OAuthLoginSuccessHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
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
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtUtil: JwtUtil,
    private val oauth2LoginSuccessHandler: OAuthLoginSuccessHandler,
    private val oAuthLoginFailureHandler: OAuthLoginFailureHandler,

    @Value("\${cookie.token.access-token}")
    private val accessTokenName : String,

    private val objectMapper: ObjectMapper
) {

    @Bean
    fun configure(): WebSecurityCustomizer? {
        return WebSecurityCustomizer { web: WebSecurity ->
            //로그인이 아예 안되어 있어도 괜찮은 api
            //해당 ""에 API 추가시 해당 API는 필터를 거치지 않음.
            web.ignoring().requestMatchers("/api/token/**")
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
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
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
//                        .requestMatchers("").permitAll()
                        .requestMatchers("/**").authenticated()
                        .anyRequest().authenticated()//로그인 이후엔 모두 허용
                }
            )
            .oauth2Login { oauth: OAuth2LoginConfigurer<HttpSecurity?> ->  // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                oauth
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
                .addFilterBefore(JwtTokenFilter(jwtUtil = jwtUtil,ACCESSTOKEN_COOKIE=accessTokenName), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(ExceptionHandlerFilter(objectMapper), JwtTokenFilter::class.java)



        return httpSecurity.build()

    }
}