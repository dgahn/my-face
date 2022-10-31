package com.pay_here.my_face.security.jwt

import mu.KotlinLogging
import org.springframework.security.core.context.SecurityContextHolder
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
        val accessTokenStatus = processingAccessToken(request)
        processingRefreshToken(accessTokenStatus, request, response)
        filterChain.doFilter(request, response)
    }

    private fun processingAccessToken(request: HttpServletRequest): JwtStatus {
        val accessToken = resolveToken(request, AUTHORIZATION_HEADER)
        val accessTokenStatus = tokenProvider.validateToken(accessToken)
        if (accessTokenStatus == JwtStatus.ACCESS) {
            val authentication = tokenProvider.getAuthentication(accessToken)
            SecurityContextHolder.getContext().authentication = authentication
            val requestURI = request.requestURI
            logger.debug { "Security Context에 '${authentication.name}' 인증 정보를 저장했습니다, uri: $requestURI" }
        }
        return accessTokenStatus
    }

    private fun processingRefreshToken(
        accessTokenStatus: JwtStatus,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        if (accessTokenStatus == JwtStatus.EXPIRED) {
            val refreshToken = resolveToken(request, REFRESH_HEADER)
            if (tokenProvider.validateToken(refreshToken) === JwtStatus.ACCESS) {
                val newRefresh = tokenProvider.reissueRefreshToken(refreshToken)
                response.setTokenHeader(REFRESH_HEADER, newRefresh)
                val authentication = tokenProvider.getAuthentication(refreshToken)
                response.setTokenHeader(AUTHORIZATION_HEADER, tokenProvider.createToken(authentication))
                SecurityContextHolder.getContext().authentication = authentication
                logger.info { "Refresh Token과 Access Token을 재발급 하였습니다." }
            }
        }
    }

    private fun resolveToken(request: HttpServletRequest, header: String): String {
        val bearerToken = runCatching { request.getHeader(header) }.getOrNull()
        require(bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            "header에 ${header}값이 존재하지 않습니다."
        }
        return bearerToken.substring(BEARER_TOKEN_START_INDEX)
    }

    private fun HttpServletResponse.setTokenHeader(header: String, token: String) {
        this.setHeader(header, "$TOKEN_PREFIX $token")
    }

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        private const val REFRESH_HEADER = "Refresh"
        private const val TOKEN_PREFIX = "Bearer "
        private const val BEARER_TOKEN_START_INDEX = 7
        private val logger = KotlinLogging.logger { }
    }
}
