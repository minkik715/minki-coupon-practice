package io.github.minkik715.coupon.couponconsumer

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties=["spring.config.name=application-core"])
@ActiveProfiles("test")
class CouponEntityConsumerApplicationTests {

    @Test
    fun contextLoads() {
    }

}
