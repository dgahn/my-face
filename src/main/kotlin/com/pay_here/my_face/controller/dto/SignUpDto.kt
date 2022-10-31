package com.pay_here.my_face.controller.dto

data class SignUpRequestDto(
    val email: String,
    val password: String
)

data class SignUpResponseDto(
    val email: String
)
