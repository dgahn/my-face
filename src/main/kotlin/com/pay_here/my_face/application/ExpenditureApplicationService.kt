package com.pay_here.my_face.application

import com.pay_here.my_face.domain.Expenditure
import com.pay_here.my_face.domain.ExpenditureJpaRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExpenditureApplicationService(
    private val expenditureJpaRepository: ExpenditureJpaRepository
) {
    @Transactional
    fun createExpenditure(expenditure: Expenditure): Expenditure {
        // ToDo email과 토큰을 비교하는 로직 추가 필요.
        return expenditureJpaRepository.save(expenditure)
    }

    @Transactional(readOnly = true)
    fun search(email: String, isDeleted: Boolean, page: Int, size: Int): List<Expenditure> {
        return expenditureJpaRepository.findAllByEmailAndIsDeleted(email, isDeleted, PageRequest.of(page - 1, size))
    }

    @Transactional
    fun updateExpenditure(id: Long, money: Long, memo: String): Expenditure {
        val expenditure = getById(id)
        expenditure.update(memo, money)
        return expenditure
    }

    @Transactional
    fun deleteExpenditure(id: Long) {
        val expenditure = getById(id)
        expenditure.delete()
    }

    private fun getById(id: Long): Expenditure =
        expenditureJpaRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("지출내역이 존재하지 않습니다. (id: $id)")
}
