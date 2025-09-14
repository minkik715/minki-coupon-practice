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
    private val distributeLockExecutor: DistributeLockExecutor
) {

    fun issueV1(couponId: Long, userId: Long){
        val key = "issue.request.sorted_set.couponId=$couponId"
        //redisRepository.zAdd(key, userId.toString(), System.currentTimeMillis().toDouble())
    }


    fun issueV2(couponId: Long, userId: Long){
        val coupon = couponIssueService.findCoupon(couponId)
        val totalQuantity = coupon.getTotalQuantity()

        if(!coupon.availableIssueDate()){
            throw CouponIssueException("발급 가능한 일자가 아닙니다, request: ${LocalDateTime.now()}, issuedStart: ${coupon.getDateIssueStart()}, issuedEnd: ${coupon.getDateIssueEnd()}", ErrorCode.INVALID_COUPON_ISSUE_DATE)
        }

        distributeLockExecutor.execute("lock_coupon_issue_${couponId}",
            {
                if(!couponIssueRedisService.availableTotalIssueQuantity(couponId, totalQuantity)){
                    throw CouponIssueException("발급 가능한 수량을 초과했습니다. total: $totalQuantity, couponId: $couponId", ErrorCode.INVALID_COUPON_ISSUE_QUANTITY)
                }

                if(!couponIssueRedisService.availableUserIssueQuantity(couponId, userId)){
                    throw CouponIssueException("쿠폰이 이미 발급되었습니다. userId: $userId, couponId: $couponId", ErrorCode.COUPON_ALREADY_ISSUED)
                }

                couponIssueRedisService.couponIssue(couponId, userId)
            },
            3000,
            3000
        )



        // 쿠폰 큐에 적재 (큐에 적재하는 로직 필요)
    }




}