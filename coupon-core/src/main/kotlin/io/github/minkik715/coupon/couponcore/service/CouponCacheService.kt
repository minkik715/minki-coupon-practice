package io.github.minkik715.coupon.couponcore.service

import io.github.minkik715.coupon.couponcore.repository.redis.dto.CouponRedisEntity
import org.springframework.aop.framework.AopContext
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CouponCacheService(
    private val couponIssueService: CouponIssueService,
) {

    //cacheEviction on update
    @Cacheable(cacheNames =  ["cache.coupon"], cacheManager = "redisCacheManager" )
    fun getCouponCache(couponId: Long): CouponRedisEntity{
        val coupon = couponIssueService.findCoupon(couponId)
        return CouponRedisEntity(coupon)
    }

    @CachePut(cacheNames =  ["cache.coupon"], cacheManager = "redisCacheManager" )
    fun putCouponCache(couponId: Long): CouponRedisEntity{
        return getCouponCache(couponId)
    }

    @Cacheable(cacheNames =  ["cache.coupon"], cacheManager = "localCacheManager" )
    fun getCouponLocalCache(couponId: Long): CouponRedisEntity{
        return proxy().getCouponCache(couponId)
    }



    @CachePut(cacheNames =  ["cache.coupon"], cacheManager = "localCacheManager" )
    fun putCouponLocalCache(couponId: Long): CouponRedisEntity{
        return getCouponLocalCache(couponId)
    }


    private fun proxy() = AopContext.currentProxy() as CouponCacheService


}