package io.github.minkik715.coupon.couponconsumer.component

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.minkik715.coupon.couponconsumer.config.TestConfig
import io.github.minkik715.coupon.couponcore.entity.CouponEntity
import io.github.minkik715.coupon.couponcore.entity.CouponType
import io.github.minkik715.coupon.couponcore.repository.mysql.CouponIssueJpaRepository
import io.github.minkik715.coupon.couponcore.repository.mysql.CouponJpaRepository
import io.github.minkik715.coupon.couponcore.repository.redis.RedisRepository
import io.github.minkik715.coupon.couponcore.service.CouponIssueService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.LocalDateTime

@Import(value = [CouponIssueListener::class] )
class CouponIssueListenerTest : TestConfig(){

    @Autowired
    lateinit var couponIssueListener: CouponIssueListener

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    lateinit var couponJpaRepository: CouponJpaRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var couponIssueJpaRepository: CouponIssueJpaRepository

    @Autowired
    lateinit var redisRepository: RedisRepository

    @Autowired
    lateinit var couponIssueService: CouponIssueService


    @BeforeEach
    fun clear(){
        redisTemplate.keys("*").let {
            redisTemplate.delete(it)
        }
    }

    @Test
    @DisplayName("쿠폰 발급 Listener - 큐에 데이터가 있다면 발급한다.")
    fun test(){
        val userId = 1L;
        val totalQuantity = 100;

        val coupon = CouponEntity(
            type = CouponType.FIRST_COME_FIRST_SERVED,
            totalQuantity = 100,
            issuedQuantity = 0,
            title = "선착순 테스트 쿠폰",
            dateIssueStart = LocalDateTime.now().minusDays(1),
            dateIssueEnd = LocalDateTime.now().plusDays(1),
            discountAmount = 1000,
            minAvailableAmount= 3000
        )
        couponJpaRepository.save(coupon)

        redisRepository.issueRequest(coupon.getId(), userId, totalQuantity)

        couponIssueListener.listenCouponIssue()

        couponJpaRepository.findById(coupon.getId()).let {
            assert(it.isPresent && it.get().getIssuedQuantity() ==1)
        }

        couponIssueJpaRepository.findCouponIssueEntityByCouponIdAndUserId(coupon.getId(), userId).let {
            assert(it != null)
        }
    }
}