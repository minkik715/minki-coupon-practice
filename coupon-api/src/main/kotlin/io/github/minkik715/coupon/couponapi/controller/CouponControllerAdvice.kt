package io.github.minkik715.coupon.couponapi.controller

import io.github.minkik715.coupon.couponapi.controller.dto.CouponIssueResponseDto
import io.github.minkik715.coupon.couponcore.exception.CouponIssueException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CouponControllerAdvice {


    @ExceptionHandler(CouponIssueException::class)
    fun handleException(e: CouponIssueException): ResponseEntity<CouponIssueResponseDto> {
        val responseDto = CouponIssueResponseDto(false, e.errorCode.msg)
        return ResponseEntity.badRequest().body(responseDto)
    }
}