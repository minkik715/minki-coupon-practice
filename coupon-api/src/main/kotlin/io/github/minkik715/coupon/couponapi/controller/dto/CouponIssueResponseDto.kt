package io.github.minkik715.coupon.couponapi.controller.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CouponIssueResponseDto(
    val success: Boolean,
    val comment: String?=null,
)