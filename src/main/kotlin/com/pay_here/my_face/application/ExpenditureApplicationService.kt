package com.pay_here.my_face.application

import com.pay_here.my_face.domain.Expenditure
import com.pay_here.my_face.domain.ExpenditureJpaRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ExpenditureApplicationService(
    private val expenditureJpaRepository: ExpenditureJpaRepository
) {
    fun createExpenditure(expenditure: Expenditure): Expenditure {
        // ToDo email과 토큰을 비교하는 로직 추가 필요.
        return expenditureJpaRepository.save(expenditure)
    }

    fun search(email: String, page: Int, size: Int): List<Expenditure> {
        return expenditureJpaRepository.findAllByEmail(email, PageRequest.of(page - 1, size))
    }
}
