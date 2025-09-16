package io.github.minkik715.coupon.couponconsumer.component

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.minkik715.coupon.couponcore.repository.redis.RedisRepository
import io.github.minkik715.coupon.couponcore.repository.redis.dto.CouponIssueConfirmDTO
import io.github.minkik715.coupon.couponcore.service.CouponIssueService
import io.github.minkik715.coupon.couponcore.util.CouponRedisHelper
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@EnableScheduling
@Component
class CouponIssueListener(
    private val redisRepository: RedisRepository,
    private val couponIssueService: CouponIssueService,
    private val objectMapper: ObjectMapper
) {

    val log = LoggerFactory.getLogger(CouponIssueListener::class.java)

    @Scheduled(fixedDelay = 1L, timeUnit = TimeUnit.SECONDS)
    fun listenCouponIssue() {
        log.info("쿠폰 발급 요청 확인중...")
        while (existCouponIssueTarget()) {
            val target: CouponIssueConfirmDTO = getIssueTarget();
            log.info("쿠폰 발급 시작 ${target}")
            couponIssueService.issue(target.couponId, target.userId)
            log.info("쿠폰 발급 완료 ${target}")
        }

    }

    private fun getIssueTarget(): CouponIssueConfirmDTO {
        return redisRepository.rPop(CouponRedisHelper.COUPON_QUEUE_KEY)?.let {
            objectMapper.readValue(it, CouponIssueConfirmDTO::class.java)
        } ?: throw IllegalStateException("발급 대상이 존재하지 않습니다.")
    }

    private fun existCouponIssueTarget(): Boolean {
        return redisRepository.lSize(CouponRedisHelper.COUPON_QUEUE_KEY) > 0
    }
}