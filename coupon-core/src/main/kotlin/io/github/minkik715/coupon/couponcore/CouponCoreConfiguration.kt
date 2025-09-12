package io.github.minkik715.coupon.couponcore

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@ComponentScan
@EnableAutoConfiguration
@EnableJpaAuditing
class CouponCoreConfiguration {
}