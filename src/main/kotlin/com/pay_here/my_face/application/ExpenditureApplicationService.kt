package com.pay_here.my_face.application

import com.pay_here.my_face.domain.Expenditure
import com.pay_here.my_face.domain.ExpenditureJpaRepository
import com.pay_here.my_face.resolver.UserDto
import com.pay_here.my_face.validator.UserValidator
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExpenditureApplicationService(
    private val userValidator: UserValidator,
    private val expenditureJpaRepository: ExpenditureJpaRepository
) {
    @Transactional
    fun createExpenditure(expenditure: Expenditure, userDto: UserDto): Expenditure {
        userValidator.validate(userDto, expenditure.email)
        return expenditureJpaRepository.save(expenditure)
    }

    @Transactional(readOnly = true)
    fun search(email: String, isDeleted: Boolean, page: Int, size: Int, userDto: UserDto): List<Expenditure> {
        userValidator.validate(userDto, email)
        return expenditureJpaRepository.findAllByEmailAndIsDeleted(email, isDeleted, PageRequest.of(page - 1, size))
    }

    @Transactional
    fun updateExpenditure(id: Long, money: Long, memo: String, userDto: UserDto): Expenditure {
        val expenditure = getById(id)
        userValidator.validate(userDto, expenditure.email)
        expenditure.update(memo, money)
        return expenditure
    }

    @Transactional
    fun deleteExpenditure(id: Long, userDto: UserDto) {
        val expenditure = getById(id)
        userValidator.validate(userDto, expenditure.email)
        expenditure.delete()
    }

    private fun getById(id: Long): Expenditure =
        expenditureJpaRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("지출내역이 존재하지 않습니다. (id: $id)")

    @Transactional
    fun recoveryExpenditure(id: Long, userDto: UserDto) {
        val expenditure = getById(id)
        userValidator.validate(userDto, expenditure.email)
        expenditure.recovery()
    }
}
