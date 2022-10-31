package com.pay_here.my_face.controller

import com.pay_here.my_face.application.UserApplicationService
import com.pay_here.my_face.controller.dto.SignUpRequestDto
import com.pay_here.my_face.controller.dto.SignUpResponseDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userApplicationService: UserApplicationService
) {

    @PostMapping("/v1/sign-up")
    fun signUp(@RequestBody signUpRequestDto: SignUpRequestDto): SignUpResponseDto {
        val user = userApplicationService.signUp(signUpRequestDto.email, signUpRequestDto.password)
        return SignUpResponseDto(user.email)
    }
}
