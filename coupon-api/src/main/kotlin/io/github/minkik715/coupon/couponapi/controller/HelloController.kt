package io.github.minkik715.coupon.couponapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/hello")
    fun hello(): String {
        Thread.sleep(500)
        return "Hello World"
    }
}