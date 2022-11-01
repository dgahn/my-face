package com.pay_here.my_face.controller

import com.pay_here.my_face.application.ExpenditureApplicationService
import com.pay_here.my_face.controller.dto.CreateExpenditureRequestDto
import com.pay_here.my_face.controller.dto.CreateExpenditureResponseDto
import com.pay_here.my_face.controller.dto.SearchExpenditureResponseDto
import com.pay_here.my_face.controller.dto.UpdateExpenditureRequestDto
import com.pay_here.my_face.controller.dto.UpdateExpenditureResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@RestController
@Validated
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

    @GetMapping("/v1/expenditures")
    fun getExpenditures(
        @RequestParam email: String,
        @RequestParam(required = false, defaultValue = "1") @Min(PAGING_MIN) @Max(PAGING_MAX) page: Int,
        @RequestParam(required = false, defaultValue = "10") @Min(PAGING_MIN) @Max(PAGING_MAX) size: Int,
    ): ResponseEntity<List<SearchExpenditureResponseDto>> {
        // ToDo 식별자로 정렬 추가 필요
        return ResponseEntity.ok(
            expenditureApplicationService.search(email, page, size)
                .map { SearchExpenditureResponseDto.of(it) }
        )
    }

    @PutMapping("/v1/expenditures/{id}")
    fun updateExpenditure(
        @PathVariable("id") id: Long,
        @RequestBody request: UpdateExpenditureRequestDto
    ): ResponseEntity<UpdateExpenditureResponseDto> {
        val updatedExpenditure = expenditureApplicationService.updateExpenditure(
            id,
            request.money,
            request.memo
        )
        return ResponseEntity.ok(UpdateExpenditureResponseDto(updatedExpenditure.id))
    }

    companion object {
        private const val PAGING_MIN = 1L
        private const val PAGING_MAX = 50L
    }
}
