package com.israel.betterprofessor.controller

import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest

@RestController
class LogoutController(private val tokenStore: TokenStore) {

    @PostMapping("/user/logout")
    @ResponseStatus(HttpStatus.OK)
    fun logout(request: HttpServletRequest) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null) {
            val tokenValue = authHeader.replace("Bearer", "").trim { it <= ' ' }
            val accessToken = tokenStore.readAccessToken(tokenValue)
            tokenStore.removeAccessToken(accessToken)
        }
    }
}
