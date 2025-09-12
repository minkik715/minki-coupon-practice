package io.github.minkik715.coupon.couponcore.service

import io.github.minkik715.coupon.couponcore.config.TestConfig
import io.github.minkik715.coupon.couponcore.entity.CouponEntity
import io.github.minkik715.coupon.couponcore.exception.CouponIssueException
import io.github.minkik715.coupon.couponcore.exception.ErrorCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CouponTest :TestConfig(){

    @Test
    @DisplayName("발급 수량이 남아있다면 true를 반환")
    fun availableIssueQuantityTest_1() {
        val coupon = CouponEntity(
            totalQuantity = 100,
            issuedQuantity = 99,
        )

        val result = coupon.availableIssueQuantity();

        assertTrue(result)
    }

    @Test
    @DisplayName("발급 수량이 소진되었다면 false를 반환")
    fun availableIssueQuantityTest_2() {
        val coupon = CouponEntity(
            totalQuantity = 100,
            issuedQuantity = 100,
        )

        val result = coupon.availableIssueQuantity();

        assertFalse(result)
    }


    @Test
    @DisplayName("최대 수량이 설정되지 않았다면 true를 반환")
    fun availableIssueQuantityTest_3() {
        val coupon = CouponEntity(
            totalQuantity = null,
            issuedQuantity = 100,
        )

        val result = coupon.availableIssueQuantity();

        assertTrue(result)
    }

    @Test
    @DisplayName("발급 기한이 시작되지 않았다면 false를 반환한다.")
    fun availableIssueDate_1() {
        val coupon = CouponEntity(
            dateIssueStart = LocalDateTime.now().plusDays(1),
            dateIssueEnd = LocalDateTime.now().plusDays(2),
        )

        val result = coupon.availableIssueDate();

        assertFalse(result)
    }

    @Test
    @DisplayName("발급 기한에 해당되면 true를 반환한다.")
    fun availableIssueDate_2() {
        val coupon = CouponEntity(
            dateIssueStart = LocalDateTime.now().plusDays(-1),
            dateIssueEnd = LocalDateTime.now().plusDays(2),
        )

        val result = coupon.availableIssueDate();

        assertTrue(result)
    }

    @Test
    @DisplayName("발급 기한이 종료되면 false를 반환.")
    fun availableIssueDate_3() {
        val coupon = CouponEntity(
            dateIssueStart = LocalDateTime.now().plusDays(-3),
            dateIssueEnd = LocalDateTime.now().plusDays(-2),
        )

        val result = coupon.availableIssueDate();

        assertFalse(result)
    }

    @Test
    @DisplayName("발급 기한이 유효하다면 성공")
    fun couponIssueTest_1() {
        val coupon = CouponEntity(
            totalQuantity = 100,
            issuedQuantity = 99,
            dateIssueStart = LocalDateTime.now().plusDays(-1),
            dateIssueEnd = LocalDateTime.now().plusDays(1),
        )

        val result = coupon.issue();

        assertEquals(coupon.getIssuedQuantity(), 100)
    }


    @Test
    @DisplayName("발급 수량 초과시 예외를 반환")
    fun couponIssueTest_2() {
        val coupon = CouponEntity(
            totalQuantity = 100,
            issuedQuantity = 100,
            dateIssueStart = LocalDateTime.now().plusDays(-1),
            dateIssueEnd = LocalDateTime.now().plusDays(1),
        )


        val assertThrows = assertThrows(CouponIssueException::class.java) {
            coupon.issue()
        }

        assertEquals(assertThrows.errorCode, ErrorCode.INVALID_COUPON_ISSUE_QUANTITY)

    }

    @Test
    @DisplayName("발급 기간아니면 예외를 반환")
    fun couponIssueTest_3() {
        val coupon = CouponEntity(
            totalQuantity = 100,
            issuedQuantity = 99,
            dateIssueStart = LocalDateTime.now().plusDays(1),
            dateIssueEnd = LocalDateTime.now().plusDays(2),
        )


        val assertThrows = assertThrows(CouponIssueException::class.java) {
            coupon.issue()
        }
        assertEquals(assertThrows.errorCode, ErrorCode.INVALID_COUPON_ISSUE_DATE)


    }



}