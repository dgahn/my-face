package com.pay_here.my_face.resolver

import com.pay_here.my_face.security.jwt.TokenProvider
import com.pay_here.my_face.util.AUTHORIZATION_HEADER
import com.pay_here.my_face.util.resolveToken
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

class UserArgumentResolver(
    private val tokenProvider: TokenProvider
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType.equals(UserDto::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val request = webRequest.nativeRequest as HttpServletRequest
        val accessToken = request.resolveToken(AUTHORIZATION_HEADER)
        tokenProvider.validateToken(accessToken)
        val email = tokenProvider.parseTokenBody(accessToken).subject
        return UserDto(email)
    }
}
