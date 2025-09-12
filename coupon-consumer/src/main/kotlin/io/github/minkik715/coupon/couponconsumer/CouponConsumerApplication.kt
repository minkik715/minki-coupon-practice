package io.github.minkik715.coupon.couponconsumer

import io.github.minkik715.coupon.couponcore.CouponCoreConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import


@SpringBootApplication
@Import(value = [CouponCoreConfiguration::class])
class CouponConsumerApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.name", "application-consumer,application-core")
    runApplication<CouponConsumerApplication>(*args)
}
