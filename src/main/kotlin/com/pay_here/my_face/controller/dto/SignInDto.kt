package com.pay_here.my_face.controller.dto

data class SignInRequestDto(
    val email: String,
    val password: String
)

data class SignInResponseDto private constructor(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        private const val TOKEN_PREFIX = "Bearer"
        fun of(
            accessToken: String,
            refreshToken: String
        ): SignInResponseDto {
            return SignInResponseDto(
                accessToken = "$TOKEN_PREFIX $accessToken",
                refreshToken = "$TOKEN_PREFIX $refreshToken"
            )
        }
    }
}
