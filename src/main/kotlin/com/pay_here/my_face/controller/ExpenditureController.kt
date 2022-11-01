package com.pay_here.my_face.controller

import com.pay_here.my_face.application.ExpenditureApplicationService
import com.pay_here.my_face.controller.dto.CreateExpenditureRequestDto
import com.pay_here.my_face.controller.dto.CreateExpenditureResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ExpenditureController(
    private val expenditureApplicationService: ExpenditureApplicationService
) {

    @PostMapping("/v1/expenditures")
    fun createExpenditure(
        @RequestBody request: CreateExpenditureRequestDto
    ): ResponseEntity<CreateExpenditureResponseDto> {
        val expenditure = expenditureApplicationService.createExpenditure(request.toEntity())
        return ResponseEntity.ok(CreateExpenditureResponseDto(expenditure.id))
    }
}
