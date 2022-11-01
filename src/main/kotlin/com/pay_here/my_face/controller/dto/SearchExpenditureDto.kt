package com.pay_here.my_face.controller.dto

import com.pay_here.my_face.domain.Expenditure
import java.time.Instant

data class SearchExpenditureResponseDto(
    val id: Long,
    val money: Long,
    val memo: String,
    val email: String,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    companion object {
        fun of(expenditure: Expenditure): SearchExpenditureResponseDto {
            return SearchExpenditureResponseDto(
                id = expenditure.id,
                money = expenditure.money,
                memo = expenditure.memo,
                email = expenditure.email,
                createdAt = expenditure.createdAt!!,
                updatedAt = expenditure.updatedAt!!
            )
        }
    }
}
