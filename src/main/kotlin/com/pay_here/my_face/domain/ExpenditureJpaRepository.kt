package com.pay_here.my_face.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ExpenditureJpaRepository : JpaRepository<Expenditure, Long> {
    fun findAllByEmailAndIsDeleted(email: String, isDeleted: Boolean, pageable: Pageable): List<Expenditure>
}
