package io.github.minkik715.coupon.couponcore.component

import io.github.minkik715.coupon.couponcore.entity.event.CouponIssueCompleteEvent
import io.github.minkik715.coupon.couponcore.service.CouponCacheService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CouponEventListener(
    private val couponCacheService: CouponCacheService
) {

    private val log = LoggerFactory.getLogger(CouponEventListener::class.java)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun issueComplete(event: CouponIssueCompleteEvent) {
        log.info("쿠폰 발급 완료 이벤트 수신: couponId: ${event.couponId}")
        couponCacheService.putCouponCache(event.couponId)
        couponCacheService.putCouponLocalCache(event.couponId)
        log.info("쿠폰 캐시 갱신 완료: couponId: ${event.couponId}")
    }
}