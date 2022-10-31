package com.pay_here.my_face.security.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtFilter(private val tokenProvider: TokenProvider) : OncePerRequestFilter() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = resolveToken(request)
        val requestURI = request.requestURI
        val authentication = tokenProvider.getAuthentication(jwt)
        SecurityContextHolder.getContext().authentication = authentication
        logger.debug { "Security Context에 '${authentication.name}' 인증 정보를 저장했습니다, uri: $requestURI" }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        require(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            "유효한 JWT 토큰이 없습니다"
        }
        val jwt = bearerToken.substring(BEARER_TOKEN_START_INDEX)
        require(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            "유효한 JWT 토큰이 없습니다"
        }
        return jwt
    }

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_TOKEN_START_INDEX = 7
    }
}
