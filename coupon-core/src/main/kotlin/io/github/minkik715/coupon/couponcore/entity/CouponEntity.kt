package io.github.minkik715.coupon.couponcore.entity

import io.github.minkik715.coupon.couponcore.exception.CouponIssueException
import io.github.minkik715.coupon.couponcore.exception.ErrorCode
import jakarta.persistence.*
import lombok.Getter
import java.time.LocalDateTime

@Entity
@Table(name = "coupons")
class CouponEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long = 0L,

    @Column(nullable = false)
    private var title: String? = null,


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private var type: CouponType? = null,

    private var totalQuantity: Int? = null,

    private var issuedQuantity: Int = 0,



    @Column(nullable = false)
    private var discountAmount: Int? = null,

    @Column(nullable = false)
    private var minAvailableAmount: Int? = null,

    @Column(nullable = false)
    private var dateIssueStart: LocalDateTime? = null,

    @Column(nullable = false)
    private var dateIssueEnd: LocalDateTime? = null,

    ) : BaseTimeEntity(){
        fun getId() = id
    fun getIssuedQuantity() = issuedQuantity

    fun getTotalQuantity() = totalQuantity!!
    fun getDateIssueStart() = dateIssueStart!!
    fun getDateIssueEnd() = dateIssueEnd!!
    fun getType() = type!!

    fun availableIssueQuantity(): Boolean{
        return totalQuantity?.let {
            it > issuedQuantity
        }?: true
    }

    fun availableIssueDate(): Boolean {
        val now = LocalDateTime.now()
        return dateIssueStart?.isBefore(now) == true && dateIssueEnd?.isAfter(now) == true
    }

    fun issue(){
        if(!availableIssueDate()){
            throw CouponIssueException("발급 가능한 일자가 아닙니다, request: ${LocalDateTime.now()}, issuedStart: $dateIssueStart, issuedEnd: $dateIssueEnd", ErrorCode.INVALID_COUPON_ISSUE_DATE)
        }
        if(!availableIssueQuantity()){
            throw CouponIssueException("발급 가능한 수량을 초과했습니다. total: $totalQuantity, issued: $issuedQuantity", ErrorCode.INVALID_COUPON_ISSUE_QUANTITY)

        }
        issuedQuantity += 1
    }

}