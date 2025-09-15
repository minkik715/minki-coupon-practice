package io.github.minkik715.coupon.couponcore

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableCaching
@ComponentScan
@EnableAutoConfiguration
@EnableJpaAuditing
class CouponCoreConfiguration {
}