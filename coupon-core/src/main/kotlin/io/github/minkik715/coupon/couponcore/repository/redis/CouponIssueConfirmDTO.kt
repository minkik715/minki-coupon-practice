package io.github.minkik715.coupon.couponcore.repository.redis

data class CouponIssueConfirmDTO(
    val userId: Long,
    val couponId: Long
)
