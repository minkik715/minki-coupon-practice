package io.github.minkik715.coupon.couponconsumer.config

import io.github.minkik715.coupon.couponcore.CouponCoreConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional

@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = ["spring.config.name=application-core"])
@SpringBootTest(classes = [CouponCoreConfiguration::class])
class TestConfig {
}