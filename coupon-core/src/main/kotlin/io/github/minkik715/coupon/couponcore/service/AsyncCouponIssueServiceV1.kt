package io.github.minkik715.coupon.couponcore.service

import io.github.minkik715.coupon.couponcore.component.DistributeLockExecutor
import io.github.minkik715.coupon.couponcore.exception.CouponIssueException
import io.github.minkik715.coupon.couponcore.exception.ErrorCode
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AsyncCouponIssueServiceV1(
    private val couponIssueRedisService: CouponIssueRedisService,
    private val couponIssueService: CouponIssueService,
    private val couponCacheService: CouponCacheService,
    private val distributeLockExecutor: DistributeLockExecutor
) {

    fun issueV1(couponId: Long, userId: Long){
        val key = "issue.request.sorted_set.couponId=$couponId"
        //redisRepository.zAdd(key, userId.toString(), System.currentTimeMillis().toDouble())
    }


    fun issueV2(couponId: Long, userId: Long){
        val coupon = couponCacheService.getCouponLocalCache(couponId)


        distributeLockExecutor.execute("lock_coupon_issue_${couponId}",
            {
                couponIssueRedisService.couponIssue(coupon, userId)
            },
            3000,
            3000
        )
    }

    fun issueV3(couponId: Long, userId: Long){
        val coupon = couponCacheService.getCouponLocalCache(couponId)
        couponIssueRedisService.couponIssueV2(coupon, userId)
    }

    fun issueV4(couponId: Long, userId: Long){
        val coupon = couponCacheService.getCouponLocalCache(couponId)
        couponIssueRedisService.couponIssueV3(coupon, userId)
    }




}