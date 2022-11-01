package com.pay_here.my_face.validator

import com.pay_here.my_face.resolver.UserDto
import org.springframework.stereotype.Component

@Component
class UserValidator {
    fun validate(userDto: UserDto, email: String) {
        require(userDto.email == email) { "토큰의 이메일 정보와 요청 이메일 정보가 다릅니다." }
    }
}
