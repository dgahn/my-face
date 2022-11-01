package com.pay_here.my_face.security.jwt

import com.pay_here.my_face.util.AUTHORIZATION_HEADER
import com.pay_here.my_face.util.REFRESH_HEADER
import com.pay_here.my_face.util.resolveToken
import com.pay_here.my_face.util.setTokenHeader
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
        val accessToken = request.resolveToken(AUTHORIZATION_HEADER)
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
            val refreshToken = request.resolveToken(REFRESH_HEADER)
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

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
