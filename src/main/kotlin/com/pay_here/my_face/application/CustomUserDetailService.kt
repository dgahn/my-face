package com.pay_here.my_face.application

import com.pay_here.my_face.domain.UserJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component("userDetailService")
class CustomUserDetailService(
    private val userJpaRepository: UserJpaRepository
) : UserDetailsService {
    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        val findUser = userJpaRepository.findByIdOrNull(username) ?: throw IllegalArgumentException(
            "존재하지 않는 이메일입니다. ($username)"
        )
        return User(
            findUser.email,
            findUser.password,
            listOf()
        )
    }
}
