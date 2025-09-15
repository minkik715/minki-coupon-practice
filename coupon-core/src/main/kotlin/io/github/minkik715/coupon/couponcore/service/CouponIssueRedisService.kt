package io.github.minkik715.coupon.couponcore.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.minkik715.coupon.couponcore.exception.CouponIssueException
import io.github.minkik715.coupon.couponcore.exception.ErrorCode
import io.github.minkik715.coupon.couponcore.repository.redis.dto.CouponIssueConfirmDTO
import io.github.minkik715.coupon.couponcore.repository.redis.RedisRepository
import io.github.minkik715.coupon.couponcore.repository.redis.dto.CouponRedisEntity
import io.github.minkik715.coupon.couponcore.util.CouponRedisHelper
import io.github.minkik715.coupon.couponcore.util.CouponRedisHelper.Companion.COUPON_QUEUE_KEY
import org.springframework.stereotype.Service

@Service
class CouponIssueRedisService(
    private val redisRepository: RedisRepository,
    private val objectMapper: ObjectMapper
) {
    //TODO 레디스 작업 트랜잭션 반드시 처리해야함!
    fun couponIssue(coupon: CouponRedisEntity, userId: Long){
        val couponId = coupon.id
        coupon.checkIssuableCoupon()
        checkCouponIssueQuantity(coupon, userId)

        //TODO 트랜잭션
        redisRepository.sAdd(CouponRedisHelper.generateRequestCouponKey(couponId), userId.toString())
        redisRepository.rPush(
            COUPON_QUEUE_KEY,
            objectMapper.writeValueAsString(CouponIssueConfirmDTO(userId, couponId))
        )
    }

    fun couponIssueV2(coupon: CouponRedisEntity, userId: Long){
        val couponId = coupon.id
        coupon.checkIssuableCoupon()
        redisRepository.issueRequest(couponId, userId, coupon.totalQuantity)
    }

    fun checkCouponIssueQuantity(couponRedisEntity: CouponRedisEntity, userId: Long){
        if(!availableTotalIssueQuantity(couponRedisEntity.id, couponRedisEntity.totalQuantity)){
            throw CouponIssueException("발급 가능한 수량을 초과했습니다. total: ${couponRedisEntity.totalQuantity}, couponId: ${couponRedisEntity.id}", ErrorCode.INVALID_COUPON_ISSUE_QUANTITY)
        }

        if(!availableUserIssueQuantity(couponRedisEntity.id, userId)){
            throw CouponIssueException("쿠폰이 이미 발급되었습니다. userId: $userId, couponId: ${couponRedisEntity.id}", ErrorCode.COUPON_ALREADY_ISSUED)
        }
    }

    fun availableUserIssueQuantity(couponId: Long, userId: Long): Boolean {
        val key = CouponRedisHelper.generateRequestCouponKey(couponId)
        return !redisRepository.sIsmember(key, userId.toString())
    }

    fun availableTotalIssueQuantity(couponId: Long, totalQuantity: Int?): Boolean {
        if(totalQuantity == null) return true
        val key = CouponRedisHelper.generateRequestCouponKey(couponId)
        return totalQuantity > redisRepository.sCard(key)
    }
}