package com.pay_here.my_face.domain

import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, String>
