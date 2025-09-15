package io.github.minkik715.coupon.couponcore.service

import io.github.minkik715.coupon.couponcore.repository.redis.dto.CouponRedisEntity
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CouponCacheService(
    private val couponIssueService: CouponIssueService,
) {

    //cacheEviction on update
    @Cacheable(cacheNames =  ["cache.coupon"] )
    fun getCouponCache(couponId: Long): CouponRedisEntity{
        val coupon = couponIssueService.findCoupon(couponId)
        return CouponRedisEntity(coupon)
    }

}