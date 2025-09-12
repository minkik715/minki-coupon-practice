package io.github.minkik715.coupon.couponcore.repository.mysql

import com.querydsl.jpa.JPQLQueryFactory
import io.github.minkik715.coupon.couponcore.entity.CouponIssueEntity
import io.github.minkik715.coupon.couponcore.entity.QCouponIssueEntity.*
import org.springframework.stereotype.Repository

@Repository
class CouponIssueRepository(
    private val queryFactory: JPQLQueryFactory
){

    fun findByCouponIdAndUserId(couponId: Long, userId: Long): CouponIssueEntity? {
        return queryFactory
            .selectFrom(couponIssueEntity)
            .where(
                couponIssueEntity.couponId.eq(couponId)
                    .and(couponIssueEntity.userId.eq(userId))
            )
            .fetchFirst()
    }

}