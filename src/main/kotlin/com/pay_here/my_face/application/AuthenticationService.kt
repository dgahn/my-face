package com.pay_here.my_face.application

import com.pay_here.my_face.security.jwt.TokenProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val tokenProvider: TokenProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder
) {
    fun authenticate(email: String, password: String): String {
        val authenticationToken = UsernamePasswordAuthenticationToken(email, password)
        val authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication = authentication
        return tokenProvider.createToken(authentication)
    }
}
