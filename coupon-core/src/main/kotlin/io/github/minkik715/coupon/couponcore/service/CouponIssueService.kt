package io.github.minkik715.coupon.couponcore.service

import io.github.minkik715.coupon.couponcore.entity.CouponEntity
import io.github.minkik715.coupon.couponcore.entity.CouponIssueEntity
import io.github.minkik715.coupon.couponcore.exception.CouponIssueException
import io.github.minkik715.coupon.couponcore.exception.ErrorCode
import io.github.minkik715.coupon.couponcore.repository.mysql.CouponIssueJpaRepository
import io.github.minkik715.coupon.couponcore.repository.mysql.CouponIssueRepository
import io.github.minkik715.coupon.couponcore.repository.mysql.CouponJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponIssueService(
    private val couponJpaRepository: CouponJpaRepository,
    private val couponIssueJpaRepository: CouponIssueJpaRepository,
    private val couponIssueRepository: CouponIssueRepository,
) {
    @Transactional
    fun issue(couponId: Long, userId: Long){
        val coupon = findCoupon(couponId);
        println(coupon)
        coupon.issue()
        saveCouponIssue(couponId, userId)
    }

    @Transactional
    fun issueV3(couponId: Long, userId: Long){
        val coupon = findCouponWithLockV3(couponId);
        coupon.issue()
        saveCouponIssue(couponId, userId)
    }

    @Transactional
    fun saveCouponIssue(couponId: Long, userId: Long): CouponIssueEntity {
        checkAlreadyIssuance(couponId, userId)
        val issue = CouponIssueEntity(
            couponId = couponId,
            userId = userId
        )

        return couponIssueJpaRepository.save(issue)
    }

    private fun checkAlreadyIssuance(couponId: Long, userId: Long) {
        couponIssueRepository.findByCouponIdAndUserId(couponId, userId)?.run {
            throw CouponIssueException("쿠폰이 이미 발급되었습니다. userId: $userId, couponId: $couponId", ErrorCode.COUPON_ALREADY_ISSUED)
        }
    }

    @Transactional(readOnly = true)
    fun findCoupon(couponId: Long): CouponEntity{
        return couponJpaRepository.findById(couponId).orElseThrow {
            CouponIssueException("쿠폰이 존재하지 않습니다, couponId: $couponId", ErrorCode.COUPON_NOT_EXIST)
        }
    }

    @Transactional(readOnly = true)
    fun findCouponWithLockV3(couponId: Long): CouponEntity{
        return couponJpaRepository.findCouponByIdWithXLock(couponId)?: throw
            CouponIssueException("쿠폰이 존재하지 않습니다, couponId: $couponId", ErrorCode.COUPON_NOT_EXIST)

    }
}