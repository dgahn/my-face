package com.pay_here.my_face.application

import com.pay_here.my_face.domain.RefreshTokenJpaRepository
import com.pay_here.my_face.domain.User
import com.pay_here.my_face.domain.UserJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserApplicationService(
    private val userDomainService: UserDomainService,
    private val userJpaRepository: UserJpaRepository,
    private val refreshTokenJpaRepository: RefreshTokenJpaRepository
) {
    @Transactional
    fun signUp(email: String, password: String): User {
        if (userDomainService.existByEmail(email)) {
            throw IllegalArgumentException("이미 가입한 이메일입니다.")
        }

        val user = userDomainService.create(email, password)
        return userJpaRepository.save(user)
    }

    @Transactional
    fun signOut(email: String) {
        refreshTokenJpaRepository.deleteById(email)
    }
}
