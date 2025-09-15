package io.github.minkik715.coupon.couponcore.repository.redis.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import io.github.minkik715.coupon.couponcore.entity.CouponEntity
import io.github.minkik715.coupon.couponcore.entity.CouponType
import io.github.minkik715.coupon.couponcore.exception.CouponIssueException
import io.github.minkik715.coupon.couponcore.exception.ErrorCode
import java.time.LocalDateTime

class CouponRedisEntity(
    val id: Long,
    val type: CouponType,
    val totalQuantity: Int,

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val dateIssueStart: LocalDateTime,

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val dateIssueEnd: LocalDateTime,
){

    constructor(couponEntity: CouponEntity): this(
        id = couponEntity.getId(),
        type = couponEntity.getType(),
        totalQuantity = couponEntity.getTotalQuantity(),
        dateIssueStart = couponEntity.getDateIssueStart(),
        dateIssueEnd = couponEntity.getDateIssueEnd()
    ){

    }

    private fun availableIssueDate(): Boolean {
        val now = LocalDateTime.now()
        return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now)
    }

    fun checkIssuableCoupon(){
        if(!availableIssueDate()){
            throw CouponIssueException("발급 가능한 일자가 아닙니다, request: ${LocalDateTime.now()}, issuedStart: $dateIssueStart, issuedEnd: $dateIssueEnd", ErrorCode.INVALID_COUPON_ISSUE_DATE)
        }
    }
}
