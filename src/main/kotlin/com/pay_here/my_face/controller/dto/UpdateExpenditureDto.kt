package com.pay_here.my_face.controller.dto

data class UpdateExpenditureRequestDto(
    val email: String,
    val money: Long,
    val memo: String
)

data class UpdateExpenditureResponseDto(
    val id: Long
)
