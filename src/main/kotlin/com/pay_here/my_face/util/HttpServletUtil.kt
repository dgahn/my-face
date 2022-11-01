package com.pay_here.my_face.util

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val TOKEN_PREFIX = "Bearer "
private const val BEARER_TOKEN_START_INDEX = 7
const val AUTHORIZATION_HEADER = "Authorization"
const val REFRESH_HEADER = "Refresh"

fun HttpServletResponse.setTokenHeader(header: String, token: String) {
    this.setHeader(header, "$TOKEN_PREFIX $token")
}

fun HttpServletRequest.resolveToken(header: String): String {
    val bearerToken = runCatching { this.getHeader(header) }.getOrNull()
    require(bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
        "header에 ${header}값이 존재하지 않습니다."
    }
    return bearerToken.substring(BEARER_TOKEN_START_INDEX)
}
