package io.github.minkik715.coupon.couponcore.service

import io.github.minkik715.coupon.couponcore.config.TestConfig
import io.github.minkik715.coupon.couponcore.entity.CouponEntity
import io.github.minkik715.coupon.couponcore.entity.CouponIssueEntity
import io.github.minkik715.coupon.couponcore.entity.CouponType
import io.github.minkik715.coupon.couponcore.exception.CouponIssueException
import io.github.minkik715.coupon.couponcore.exception.ErrorCode
import io.github.minkik715.coupon.couponcore.repository.mysql.CouponIssueJpaRepository
import io.github.minkik715.coupon.couponcore.repository.mysql.CouponIssueRepository
import io.github.minkik715.coupon.couponcore.repository.mysql.CouponJpaRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull


class CouponIssueServiceTest : TestConfig() {

    @Autowired
    lateinit var couponIssueService: CouponIssueService

    @Autowired
    lateinit var couponIssueRepository: CouponIssueRepository

    @Autowired
    lateinit var couponJpaRepository: CouponJpaRepository

    @Autowired
    lateinit var couponIssueJpaRepository: CouponIssueJpaRepository

    @BeforeEach
    fun clean(){
        couponJpaRepository.deleteAllInBatch()
        couponIssueJpaRepository.deleteAllInBatch()
    }


    @Test
    @DisplayName("쿠폰 발급 내역이 존재하면 예외를 반환한다.")
    fun couponIssueTest(){
        val couponIssueEntity = CouponIssueEntity(
            userId = 1L,
            couponId = 1L,
        )

        couponIssueJpaRepository.save(couponIssueEntity)

        val error = Assertions.assertThrows(
            CouponIssueException::class.java,
            { couponIssueService.saveCouponIssue(1L, 1L) }

        )

        Assertions.assertEquals(error.errorCode, ErrorCode.COUPON_ALREADY_ISSUED)
    }

    @Test
    @DisplayName("쿠폰 발급 내역이 존재하지 않으면 쿠폰을 발급.")
    fun couponIssueTest_2(){
        val userId = 1L
        val couponId = 1L
        val saveCouponIssue = couponIssueService.saveCouponIssue(couponId, userId)

        Assertions.assertTrue(couponIssueJpaRepository.findById(saveCouponIssue.getId()).isPresent)
    }

    @Test
    @DisplayName("발급 수량, 기한, 중복 발급 문제가 없다면 쿠폰을 발급한다.")
    fun issueTest_1(){
        val coupon = CouponEntity(
            type = CouponType.FIRST_COME_FIRST_SERVED,
            totalQuantity = 100,
            issuedQuantity = 0,
            title = "선착순 테스트 쿠폰",
            dateIssueStart = LocalDateTime.now().minusDays(1),
            dateIssueEnd = LocalDateTime.now().plusDays(1),
            discountAmount = 1000,
            minAvailableAmount= 3000
        )
        val savedCoupon = couponJpaRepository.save(coupon)

        couponIssueService.issue(savedCoupon.getId(), 1L)

        Assertions.assertEquals(
            couponIssueRepository.findByCouponIdAndUserId(savedCoupon.getId(), 1L)?.getCouponId(),
            savedCoupon.getId()
        )
        Assertions.assertEquals(couponJpaRepository.findById(savedCoupon.getId()).getOrNull()?.getIssuedQuantity(), 1)

    }

    @Test
    @DisplayName("발급 수량에 문제가 생기면 쿠폰이 발급되지 않는다.")
    fun issueTest_2(){
        val coupon = CouponEntity(
            type = CouponType.FIRST_COME_FIRST_SERVED,
            totalQuantity = 100,
            issuedQuantity = 100,
            title = "선착순 테스트 쿠폰",
            dateIssueStart = LocalDateTime.now().minusDays(1),
            dateIssueEnd = LocalDateTime.now().plusDays(1),
            discountAmount = 1000,
            minAvailableAmount= 3000
        )
        val savedCoupon = couponJpaRepository.save(coupon)


        val error = Assertions.assertThrows(
            CouponIssueException::class.java,
            { couponIssueService.issue(savedCoupon.getId(), 1L) }

        )

        Assertions.assertEquals(error.errorCode, ErrorCode.INVALID_COUPON_ISSUE_QUANTITY)


    }

    @Test
    @DisplayName("발급 기한이 아니면 쿠폰이 발급되지 않는다.")
    fun issueTest_3(){
        val coupon = CouponEntity(
            type = CouponType.FIRST_COME_FIRST_SERVED,
            totalQuantity = 100,
            issuedQuantity = 100,
            title = "선착순 테스트 쿠폰",
            dateIssueStart = LocalDateTime.now().minusDays(2),
            dateIssueEnd = LocalDateTime.now().plusDays(-1),
            discountAmount = 1000,
            minAvailableAmount= 3000
        )
        val savedCoupon = couponJpaRepository.save(coupon)


        val error = Assertions.assertThrows(
            CouponIssueException::class.java,
            { couponIssueService.issue(savedCoupon.getId(), 1L) }

        )

        Assertions.assertEquals(error.errorCode, ErrorCode.INVALID_COUPON_ISSUE_DATE)


    }

}