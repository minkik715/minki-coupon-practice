package io.github.minkik715.coupon.couponcore.util

class CouponRedisHelper {
    companion object {
        const val COUPON_QUEUE_KEY = "issue.queue.coupon"
        fun generateRequestCouponKey(couponId: Long) ="issue.request.couponId=$couponId"

    }
}