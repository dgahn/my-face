package com.pay_here.my_face

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class App

@Suppress("SpreadOperator")
fun main(arg: Array<String>) {
    runApplication<App>(*arg)
}
