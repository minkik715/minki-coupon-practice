package io.github.minkik715.coupon.couponcore.repository.mysql

import io.github.minkik715.coupon.couponcore.entity.CouponEntity
import io.github.minkik715.coupon.couponcore.entity.CouponIssueEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponIssueJpaRepository: JpaRepository<CouponIssueEntity, Long> {
}