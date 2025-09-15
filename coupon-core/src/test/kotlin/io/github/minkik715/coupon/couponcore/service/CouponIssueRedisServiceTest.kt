package io.github.minkik715.coupon.couponcore.service

import io.github.minkik715.coupon.couponcore.config.TestConfig
import io.github.minkik715.coupon.couponcore.util.CouponRedisHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate

class CouponIssueRedisServiceTest : TestConfig(){

    @Autowired
    lateinit var couponIssueRedisService: CouponIssueRedisService

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    @BeforeEach
    fun clear(){
        redisTemplate.keys("*").let {
            redisTemplate.delete(it)
        }
    }

    @Test
    @DisplayName("쿠폰 수량 검증 - 발급 가능 수량이 존재하면 true를 반환한다.")
    fun availableTotalIssueQuantity_1(){
        val totalIssueQuantity = 10;
        val couponId = 1L;

        val available =
            couponIssueRedisService.couponIssue(couponId, totalIssueQuantity)

        Assertions.assertTrue(available)

    }


    @Test
    @DisplayName("쿠폰 수량 검증 - 발급 가능 수량이 소진되면 false를 반환한다.")
    fun availableTotalIssueQuantity_2(){
        val totalIssueQuantity = 10;
        val couponId = 1L;

        for(userId in 1..10){
            redisTemplate.opsForSet().add(CouponRedisHelper.generateRequestCouponKey(couponId), userId.toString())
        }
        val available =
            couponIssueRedisService.availableTotalIssueQuantity(couponId, totalIssueQuantity)

        Assertions.assertFalse(available)

    }

    @Test
    @DisplayName("중복 검증 - 발급 내역에 유저ID가 없으면 true를 반환한다.")
    fun availableUserIssueQuantity_1(){
        val userId = 1L;
        val couponId = 1L;

        val available =
            couponIssueRedisService.availableUserIssueQuantity(couponId, userId)

        Assertions.assertTrue(available)

    }

    @Test
    @DisplayName("중복 검증 - 발급 내역에 유저ID가 있으면 false를 반환한다.")
    fun availableUserIssueQuantity_2(){
        val userId = 1L;
        val couponId = 1L;

        redisTemplate.opsForSet().add(CouponRedisHelper.generateRequestCouponKey(couponId), userId.toString())

        val available =
            couponIssueRedisService.availableUserIssueQuantity(couponId, userId)

        Assertions.assertFalse(available)

    }

}