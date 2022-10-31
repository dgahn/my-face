package com.pay_here.my_face.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SignOutRequestDto(
    @JsonProperty("email")
    val email: String
)
