package com.pay_here.my_face.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class RefreshToken(
    @Id
    val email: String,
    token: String
) {
    var token: String = token
        protected set

    fun changeToken(token: String) {
        this.token = token
    }
}
