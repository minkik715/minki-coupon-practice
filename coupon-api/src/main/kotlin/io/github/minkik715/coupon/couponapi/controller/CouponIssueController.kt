package io.github.minkik715.coupon.couponapi.controller

import io.github.minkik715.coupon.couponapi.controller.dto.CouponIssueRequestDto
import io.github.minkik715.coupon.couponapi.controller.dto.CouponIssueResponseDto
import io.github.minkik715.coupon.couponapi.service.CouponIssueRequestService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CouponIssueController(
    private val couponIssueRequestService: CouponIssueRequestService
) {

    @PostMapping("/v1/issue")
    fun issueV1(@RequestBody requestDto: CouponIssueRequestDto): CouponIssueResponseDto {
        couponIssueRequestService.issueRequestV1(requestDto)
        return CouponIssueResponseDto(true)
    }

    @PostMapping("/v2/issue")
    //레디스 분산 Lock
    fun issueV2(@RequestBody requestDto: CouponIssueRequestDto): CouponIssueResponseDto {
        couponIssueRequestService.issueRequestV2(requestDto)
        return CouponIssueResponseDto(true)
    }

    @PostMapping("/v3/issue")
    //MYSQL 레코드 락
    fun issueV3(@RequestBody requestDto: CouponIssueRequestDto): CouponIssueResponseDto {
        couponIssueRequestService.issueRequestV3(requestDto)
        return CouponIssueResponseDto(true)
    }


    @PostMapping("/v1/issue-async")
    //레디스 레코드 락
    fun asyncIssueV1(@RequestBody requestDto: CouponIssueRequestDto): CouponIssueResponseDto {
        couponIssueRequestService.asyncIssueRequestV1(requestDto)
        return CouponIssueResponseDto(true)
    }

    @PostMapping("/v2/issue-async")
    //레디스 레코드 락
    fun asyncIssueV2(@RequestBody requestDto: CouponIssueRequestDto): CouponIssueResponseDto {
        couponIssueRequestService.asyncIssueRequestV2(requestDto)
        return CouponIssueResponseDto(true)
    }


    @PostMapping("/v3/issue-async")
    //redis lua script 사용
    fun asyncIssueV3(@RequestBody requestDto: CouponIssueRequestDto): CouponIssueResponseDto {
        couponIssueRequestService.asyncIssueRequestV3(requestDto)
        return CouponIssueResponseDto(true)
    }


    @PostMapping("/v4/issue-async")
    //redis lua script 사용 & 로컬 캐쉬 사용
    fun asyncIssueV4(@RequestBody requestDto: CouponIssueRequestDto): CouponIssueResponseDto {
        couponIssueRequestService.asyncIssueRequestV4(requestDto)
        return CouponIssueResponseDto(true)
    }
}