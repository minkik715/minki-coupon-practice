package io.github.minkik715.coupon.couponapi.service

import io.github.minkik715.coupon.couponapi.controller.dto.CouponIssueRequestDto
import io.github.minkik715.coupon.couponcore.component.DistributeLockExecutor
import io.github.minkik715.coupon.couponcore.service.AsyncCouponIssueServiceV1
import io.github.minkik715.coupon.couponcore.service.CouponIssueService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CouponIssueRequestService(
    private val couponIssueService: CouponIssueService,
    private val asyncCouponIssueServiceV1: AsyncCouponIssueServiceV1,
    private val distributeLockExecutor: DistributeLockExecutor
) {
    val log = LoggerFactory.getLogger(CouponIssueRequestService::class.java)!!

    fun issueRequestV1(requestDto: CouponIssueRequestDto){
        synchronized(this){
            couponIssueService.issue(requestDto.couponId, requestDto.userId)
        }

        log.info("쿠폰 발급 완료, couponId: ${requestDto.couponId}, userId: ${requestDto.userId}")
    }

    fun issueRequestV2(requestDto: CouponIssueRequestDto){
        distributeLockExecutor.execute(
            "lock_${requestDto.couponId}",
            {
                couponIssueService.issue(requestDto.couponId, requestDto.userId)
            }
        )

        log.info("쿠폰 발급 완료, couponId: ${requestDto.couponId}, userId: ${requestDto.userId}")
    }

    fun issueRequestV3(requestDto: CouponIssueRequestDto){
        couponIssueService.issueV3(requestDto.couponId, requestDto.userId)

        log.info("쿠폰 발급 완료, couponId: ${requestDto.couponId}, userId: ${requestDto.userId}")
    }

    fun asyncIssueRequestV1(requestDto: CouponIssueRequestDto){
        asyncCouponIssueServiceV1.issueV1(requestDto.couponId, requestDto.userId)

        log.info("쿠폰 발급 완료, couponId: ${requestDto.couponId}, userId: ${requestDto.userId}")
    }

    fun asyncIssueRequestV2(requestDto: CouponIssueRequestDto){
        asyncCouponIssueServiceV1.issueV2(requestDto.couponId, requestDto.userId)

        log.info("쿠폰 발급 완료, couponId: ${requestDto.couponId}, userId: ${requestDto.userId}")
    }

    fun asyncIssueRequestV3(requestDto: CouponIssueRequestDto){
        asyncCouponIssueServiceV1.issueV3(requestDto.couponId, requestDto.userId)

        log.info("쿠폰 발급 완료, couponId: ${requestDto.couponId}, userId: ${requestDto.userId}")
    }

}