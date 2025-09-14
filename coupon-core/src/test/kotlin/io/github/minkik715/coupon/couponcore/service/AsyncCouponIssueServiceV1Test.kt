package io.github.minkik715.coupon.couponcore.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.minkik715.coupon.couponcore.config.TestConfig
import io.github.minkik715.coupon.couponcore.entity.CouponEntity
import io.github.minkik715.coupon.couponcore.entity.CouponType
import io.github.minkik715.coupon.couponcore.exception.CouponIssueException
import io.github.minkik715.coupon.couponcore.exception.ErrorCode
import io.github.minkik715.coupon.couponcore.repository.mysql.CouponJpaRepository
import io.github.minkik715.coupon.couponcore.repository.redis.CouponIssueConfirmDTO
import io.github.minkik715.coupon.couponcore.util.CouponRedisHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import java.time.LocalDateTime

class AsyncCouponIssueServiceV1Test : TestConfig(){

    @Autowired
    lateinit var asyncCouponIssueServiceV1: AsyncCouponIssueServiceV1

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    lateinit var couponJpaRepository: CouponJpaRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun clear(){
        couponJpaRepository.deleteAllInBatch()
        redisTemplate.keys("*").let {
            redisTemplate.delete(it)
        }
    }

    @Test
    @DisplayName("쿠폰 발급 - 쿠폰이 존재하지 않는다면 예외를 반환한다..")
    fun couponIssueTest_1(){
        val userId = 1L;
        val couponId = 1L;

        val exception = Assertions.assertThrows(
            CouponIssueException::class.java,
            { asyncCouponIssueServiceV1.issueV2(couponId, userId) }
        )
        Assertions.assertEquals(exception.errorCode,ErrorCode.COUPON_NOT_EXIST)
    }

    @Test
    @DisplayName("쿠폰 발급 - 쿠폰이 발급이 성공하는 경우 redis에 저장된다.")
    fun couponIssueTest_2(){
        val userId = 1L;
        val couponId = 1L;

        couponJpaRepository.save(
            CouponEntity(
                totalQuantity = 100,
                issuedQuantity = 30,
                title = "선착순 테스트 쿠폰",
                dateIssueStart = LocalDateTime.now().minusDays(1),
                dateIssueEnd = LocalDateTime.now().plusDays(1),
                type = CouponType.FIRST_COME_FIRST_SERVED,
                discountAmount = 1000,
                minAvailableAmount = 5000
            )
        )
        asyncCouponIssueServiceV1.issueV2(couponId, userId)

        redisTemplate.opsForSet().isMember(
            CouponRedisHelper.generateRequestCouponKey(couponId), userId.toString())
            .let {
                Assertions.assertTrue(it)
            }

        redisTemplate.opsForList().getLast(CouponRedisHelper.COUPON_QUEUE_KEY)?.let {
            val confirmDTO = objectMapper.readValue(it, CouponIssueConfirmDTO::class.java)
            Assertions.assertEquals(confirmDTO, CouponIssueConfirmDTO(userId, couponId))
        }
    }




}