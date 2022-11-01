package com.pay_here.my_face.controller.dto

import com.pay_here.my_face.domain.Expenditure

data class CreateExpenditureRequestDto(
    val email: String,
    val money: Long,
    val memo: String
) {
    fun toEntity(): Expenditure {
        return Expenditure(
            money = money,
            memo = memo,
            email = email
        )
    }
}

data class CreateExpenditureResponseDto(
    val id: Long
)
