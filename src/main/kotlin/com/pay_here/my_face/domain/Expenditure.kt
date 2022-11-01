package com.pay_here.my_face.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@EntityListeners(AuditingEntityListener::class)
class Expenditure(
    @Id
    @GeneratedValue
    val id: Long = 0,
    val email: String,
    memo: String,
    money: Long
) {
    @CreatedDate
    @Column(updatable = false)
    var createdAt: Instant? = null
        protected set

    @LastModifiedDate
    var updatedAt: Instant? = null
        protected set

    var memo: String = memo
        protected set

    var money: Long = money
        protected set(value) {
            check(value >= MIN_MONEY) { "돈은 항상 ${MIN_MONEY}원 이상이여야 합니다." }
            field = value
        }

    var isDeleted: Boolean = false
        protected set

    fun update(memo: String, money: Long) {
        this.memo = memo
        this.money = money
    }

    fun delete() {
        isDeleted = true
    }

    companion object {
        private const val MIN_MONEY = 1
    }
}
