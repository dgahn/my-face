package com.pay_here.my_face.application

import com.pay_here.my_face.domain.User
import com.pay_here.my_face.domain.UserJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDomainService(
    private val passwordEncoder: PasswordEncoder,
    private val userJpaRepository: UserJpaRepository
) {

    fun create(email: String, password: String): User {
        return User(
            email = email,
            password = passwordEncoder.encode(password)
        )
    }

    @Transactional(readOnly = true)
    fun existByEmail(email: String): Boolean {
        val user = userJpaRepository.findByIdOrNull(email)
        return user != null
    }
}
