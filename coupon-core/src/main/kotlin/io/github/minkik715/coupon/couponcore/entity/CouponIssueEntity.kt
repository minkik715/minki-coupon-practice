package io.github.minkik715.coupon.couponcore.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name = "coupon_issues")
class CouponIssueEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long = 0L,

    @Column(nullable = false)
    private var userId: Long? = null,

    @Column(nullable = false)
    private var couponId: Long? = null,

    @CreatedDate
    private var dateIssued: LocalDateTime = LocalDateTime.now(),

    private var dateUsed: LocalDateTime? = null,


    ) : BaseTimeEntity() {

    fun getId() = id

    fun getCouponId() = couponId
}