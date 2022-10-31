package com.pay_here.my_face.controller.dto

data class SignInRequestDto(
    val email: String,
    val password: String
)

data class SignInResponseDto(
    val token: String
)
