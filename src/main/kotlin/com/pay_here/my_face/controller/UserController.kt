package com.pay_here.my_face.controller

import com.pay_here.my_face.application.AuthenticationService
import com.pay_here.my_face.application.UserApplicationService
import com.pay_here.my_face.controller.dto.SignInRequestDto
import com.pay_here.my_face.controller.dto.SignInResponseDto
import com.pay_here.my_face.controller.dto.SignOutDto
import com.pay_here.my_face.controller.dto.SignUpRequestDto
import com.pay_here.my_face.controller.dto.SignUpResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userApplicationService: UserApplicationService,
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/v1/sign-up")
    fun signUp(@RequestBody request: SignUpRequestDto): ResponseEntity<SignUpResponseDto> {
        val user = userApplicationService.signUp(request.email, request.password)
        return ResponseEntity.ok(SignUpResponseDto(user.email))
    }

    @PostMapping("/v1/sign-in")
    fun signIn(@RequestBody request: SignInRequestDto): ResponseEntity<SignInResponseDto> {
        val tokens = authenticationService.authenticate(request.email, request.password)
        return ResponseEntity.ok(SignInResponseDto.of(tokens.first, tokens.second))
    }

    @PostMapping("/v1/sign-out")
    fun signOut(@RequestBody signOutDto: SignOutDto) {
        userApplicationService.signOut(signOutDto.email)
    }
}
