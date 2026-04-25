package com.tinuproject.tinu.infra.security

import com.tinuproject.tinu.factory.JwtTestFactory
import com.tinuproject.tinu.infra.security.jwt.JwtUtil
import jakarta.servlet.http.Cookie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.test.web.servlet.MockMvc
import kotlin.test.Test
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import org.springframework.test.web.servlet.get
import org.junit.jupiter.api.DisplayName

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Import(AuthTest.TestControllerConfig::class)
class AuthTest (

    @Autowired
    private val mockMvc: MockMvc,

    @Autowired
    private val jwtTokenFactory:JwtTestFactory
)
{

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @TestConfiguration
    class TestControllerConfig {

        /**
         * =========================
         * 테스트 전용 더미 Controller
         * =========================
         */
        @RestController
        internal class TestAuthController {

            @GetMapping("/test/permit-all")
            fun permitAll(): String {
                return "OK"
            }

            // 인증만 되면 OK
            @GetMapping("/test/authenticated")
            fun authenticated(@AuthenticationPrincipal userId: UUID): String {
                return userId.toString()
            }

            // USER만 OK
            @GetMapping("/test/user")
            fun user(@AuthenticationPrincipal userId: UUID): String {
                return "OK"
            }
        }
    }
    /**
     * =========================
     * 테스트 케이스
     * =========================
     */
    @Test
    @DisplayName("permitAll API는 토큰 없이 접근 가능하다")
    fun permitAll_shouldAccessibleWithoutToken() {
        mockMvc.get("/test/permit-all")
            .andExpect {
                status { isOk() }
                content { string("OK") }
            }
    }

    @Test
    @DisplayName("authenticated API는 토큰이 없으면 401을 반환한다")
    fun authenticated_shouldReturn401_whenTokenIsMissing() {
        mockMvc.get("/test/authenticated")
            .andExpect {
                status { isUnauthorized() }
            }
    }

    @Test
    @DisplayName("authenticated API는 invalid secret 토큰이면 401을 반환한다")
    fun authenticated_shouldReturn401_whenTokenHasInvalidSecret() {
        val token = jwtTokenFactory.generateInvalidSecretKey(
            uuid = UUID.randomUUID(),
            expirationMillis = 60_000,
            isSign = true
        )

        mockMvc.get("/test/authenticated") {
            cookie(Cookie("access-token", token))
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @DisplayName("authenticated API는 만료된 토큰이면 401을 반환한다")
    fun authenticated_shouldReturn401_whenTokenIsExpired() {
        val token = jwtUtil.generateAccessToken(
            uuid = UUID.randomUUID(),
            expirationMillis = -1,
            isSign = true
        )

        mockMvc.get("/test/authenticated") {
            cookie(Cookie("access-token", token))
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @DisplayName("authenticated API는 userId가 없는 토큰이면 401을 반환한다")
    fun authenticated_shouldReturn401_whenTokenHasNoUserId() {
        val token = jwtTokenFactory.generateInvalidUserID(
            expirationMillis = 60_000,
            isSign = true
        )

        mockMvc.get("/test/authenticated") {
            cookie(Cookie("access-token", token))
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @DisplayName("authenticated API는 isSign 값이 없는 토큰이면 401을 반환한다")
    fun authenticated_shouldReturn401_whenTokenHasNoIsSign() {
        val token = jwtTokenFactory.generateInvalidIsSign(
            uuid = UUID.randomUUID(),
            expirationMillis = 60_000
        )

        mockMvc.get("/test/authenticated") {
            cookie(Cookie("access-token", token))
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @DisplayName("authenticated API는 정상 토큰이면 접근 가능하다")
    fun authenticated_shouldAccessible_whenTokenIsValid() {
        val token = jwtTokenFactory.generateAccessToken(isSign = true)

        mockMvc.get("/test/authenticated") {
            cookie(Cookie("access-token", token))
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    @DisplayName("user API는 USER 권한을 가진 인증된 사용자는 접근 가능하다")
    fun userApi_shouldAccessible_whenUserIsAuthenticated() {
        val token = jwtTokenFactory.generateAccessToken(isSign = true)

        mockMvc.get("/test/user") {
            cookie(Cookie("access-token", token))
        }.andExpect {
            status { isOk() }
            content { string("OK") }
        }
    }

    @Test
    @DisplayName("user API는 isSign이 false인 토큰으로 요청 시 403을 반환한다")
    fun userApi_shouldReturn403_whenIsSignIsFalse() {
        val token = jwtTokenFactory.generateAccessToken(isSign = false)

        mockMvc.get("/test/user") {
            cookie(Cookie("access-token", token))
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @DisplayName("user API는 토큰이 없으면 401을 반환한다")
    fun userApi_shouldReturn401_whenTokenIsMissing() {
        mockMvc.get("/test/user")
            .andExpect {
                status { isUnauthorized() }
            }
    }


}