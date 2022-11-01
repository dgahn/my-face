package com.pay_here.my_face.config

import com.pay_here.my_face.resolver.UserArgumentResolver
import com.pay_here.my_face.security.jwt.TokenProvider
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val tokenProvider: TokenProvider
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(UserArgumentResolver(tokenProvider))
    }
}
