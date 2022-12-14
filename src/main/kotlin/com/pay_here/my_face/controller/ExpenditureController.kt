package com.pay_here.my_face.controller

import com.pay_here.my_face.application.ExpenditureApplicationService
import com.pay_here.my_face.controller.dto.CreateExpenditureRequestDto
import com.pay_here.my_face.controller.dto.CreateExpenditureResponseDto
import com.pay_here.my_face.controller.dto.SearchExpenditureDto
import com.pay_here.my_face.controller.dto.UpdateExpenditureRequestDto
import com.pay_here.my_face.controller.dto.UpdateExpenditureResponseDto
import com.pay_here.my_face.resolver.UserDto
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
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
        userDto: UserDto,
        @RequestBody request: CreateExpenditureRequestDto
    ): ResponseEntity<CreateExpenditureResponseDto> {
        val expenditure = expenditureApplicationService.createExpenditure(request.toEntity(), userDto)
        return ResponseEntity.ok(CreateExpenditureResponseDto(expenditure.id))
    }

    @GetMapping("/v1/expenditures")
    fun getExpenditures(
        userDto: UserDto,
        @RequestParam email: String,
        @RequestParam(required = false, defaultValue = "false") isDeleted: Boolean,
        @RequestParam(required = false, defaultValue = "1") @Min(PAGING_MIN) @Max(PAGING_MAX) page: Int,
        @RequestParam(required = false, defaultValue = "10") @Min(PAGING_MIN) @Max(PAGING_MAX) size: Int,
    ): ResponseEntity<List<SearchExpenditureDto>> {
        // ToDo ???????????? ?????? ?????? ??????
        return ResponseEntity.ok(
            expenditureApplicationService.search(email, isDeleted, page, size, userDto)
                .map { SearchExpenditureDto.of(it) }
        )
    }

    @PutMapping("/v1/expenditures/{id}")
    fun updateExpenditure(
        userDto: UserDto,
        @PathVariable("id") id: Long,
        @RequestBody request: UpdateExpenditureRequestDto
    ): ResponseEntity<UpdateExpenditureResponseDto> {
        val updatedExpenditure = expenditureApplicationService.updateExpenditure(
            id,
            request.money,
            request.memo,
            userDto,
        )
        return ResponseEntity.ok(UpdateExpenditureResponseDto(updatedExpenditure.id))
    }

    @PutMapping("/v1/expenditures/{id}/recovery")
    fun recoveryExpenditure(
        userDto: UserDto,
        @PathVariable("id") id: Long
    ) {
        expenditureApplicationService.recoveryExpenditure(id, userDto)
    }

    @DeleteMapping("/v1/expenditures/{id}")
    fun deleteExpenditure(
        userDto: UserDto,
        @PathVariable("id") id: Long
    ) {
        expenditureApplicationService.deleteExpenditure(id, userDto)
    }

    companion object {
        private const val PAGING_MIN = 1L
        private const val PAGING_MAX = 50L
    }
}
