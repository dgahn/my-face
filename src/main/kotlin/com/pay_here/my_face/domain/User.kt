package com.pay_here.my_face.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class User(
    @Id
    val email: String,
    val password: String
)
