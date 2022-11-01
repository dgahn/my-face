package com.pay_here.my_face.domain

import org.springframework.data.jpa.repository.JpaRepository

interface ExpenditureJpaRepository : JpaRepository<Expenditure, Long>
