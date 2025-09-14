package io.github.minkik715.coupon.couponcore.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.JSONPObject
import io.github.minkik715.coupon.couponcore.repository.redis.CouponIssueConfirmDTO
import io.github.minkik715.coupon.couponcore.repository.redis.RedisRepository
import io.github.minkik715.coupon.couponcore.util.CouponRedisHelper
import io.github.minkik715.coupon.couponcore.util.CouponRedisHelper.Companion.COUPON_QUEUE_KEY
import org.springframework.stereotype.Service

@Service
class CouponIssueRedisService(
    private val redisRepository: RedisRepository,
    private val objectMapper: ObjectMapper
) {

    fun availableUserIssueQuantity(couponId: Long, userId: Long): Boolean {
        val key = CouponRedisHelper.generateRequestCouponKey(couponId)
        return !redisRepository.sIsmember(key, userId.toString())
    }

    fun availableTotalIssueQuantity(couponId: Long, totalQuantity: Int?): Boolean {
        if(totalQuantity == null) return true
        val key = CouponRedisHelper.generateRequestCouponKey(couponId)
        return totalQuantity > redisRepository.sCard(key)
    }

    //TODO 레디스 작업 트랜잭션 반드시 처리해야함!
    fun couponIssue(couponId: Long, userId: Long){
        //TODO 트랜잭션
        redisRepository.sAdd(CouponRedisHelper.generateRequestCouponKey(couponId), userId.toString())
        redisRepository.rPush(
            COUPON_QUEUE_KEY,
            objectMapper.writeValueAsString(CouponIssueConfirmDTO(userId, couponId))
        )
    }
}