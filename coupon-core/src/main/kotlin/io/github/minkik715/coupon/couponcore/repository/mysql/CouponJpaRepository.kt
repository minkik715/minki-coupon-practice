package io.github.minkik715.coupon.couponcore.repository.mysql

import io.github.minkik715.coupon.couponcore.entity.CouponEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface CouponJpaRepository: JpaRepository<CouponEntity, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CouponEntity c WHERE c.id = :id")
    fun findCouponByIdWithXLock(id: Long): CouponEntity?
}