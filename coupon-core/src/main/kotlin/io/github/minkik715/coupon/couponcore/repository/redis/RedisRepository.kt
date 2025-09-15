package io.github.minkik715.coupon.couponcore.repository.redis

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.minkik715.coupon.couponcore.exception.CouponIssueException
import io.github.minkik715.coupon.couponcore.exception.ErrorCode
import io.github.minkik715.coupon.couponcore.repository.redis.dto.CouponIssueConfirmDTO
import io.github.minkik715.coupon.couponcore.util.CouponRedisHelper
import org.redisson.api.RedissonClient
import org.redisson.api.TransactionOptions
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Repository

@Repository
class RedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
    private val redissonClient: RedissonClient,
    private val objectMapper: ObjectMapper
) {
    private val issueScript = issueRequestScript()

    fun zAdd(key: String, value: String, score: Double): Boolean {
        return redisTemplate.opsForZSet().addIfAbsent(key, value, score)!!
    }

    fun sAdd(key: String, value: String): Boolean {
        return redisTemplate.opsForSet().add(key, value)!! > 0
    }

    fun sIsmember(key: String, value: String): Boolean {
        return redisTemplate.opsForSet().isMember(key, value)!!
    }

    fun sCard(key: String): Long {
        return redisTemplate.opsForSet().size(key)!!
    }

    fun rPush(key: String, value: String): Boolean {
        return redisTemplate.opsForList().rightPush(key, value)!! > 0
    }

    fun lPop(key: String): Boolean {
        return redisTemplate.opsForList().rightPop(key) != null
    }

    fun doTransaction(runnable: Runnable) {
        redissonClient.createTransaction(TransactionOptions.defaults())

    }

    fun issueRequest(couponId: Long, userId: Long, totalIssueQuantity: Int): Boolean{
        val issueRequestKey = CouponRedisHelper.generateRequestCouponKey(couponId)
        val couponIssueConfirmDTO = CouponIssueConfirmDTO(userId, couponId)


        val code = redisTemplate.execute(
            issueScript,
            listOf(issueRequestKey, CouponRedisHelper.COUPON_QUEUE_KEY),
            userId.toString(),
            totalIssueQuantity.toString(),
            objectMapper.writeValueAsString(couponIssueConfirmDTO)
        )

        return when(code){
            "1" -> true
            "2" -> throw CouponIssueException("쿠폰이 이미 발급되었습니다. userId: $userId, couponId: $couponId", ErrorCode.COUPON_ALREADY_ISSUED)
            "3" -> throw CouponIssueException("발급 가능한 수량을 초과했습니다. total: $totalIssueQuantity, couponId: $couponId", ErrorCode.INVALID_COUPON_ISSUE_QUANTITY)
            else -> throw CouponIssueException("시스템 오류로 인해 쿠폰 발급에 실패했습니다, couponId: $couponId", ErrorCode.COUPON_ISSUE_FAIL)
        }


    }

    private fun issueRequestScript(): RedisScript<String> {
        val script = """
            if redis.call("SISMEMBER", KEYS[1], ARGV[1]) ==1 then
                return '2'
            end
            if tonumber(ARGV[2]) > redis.call('SCARD', KEYS[1]) then
                redis.call('SADD', KEYS[1], ARGV[1])
                redis.call('LPUSH', KEYS[2], ARGV[3])
                return '1'
            end
            
            return '3'
        """

        return RedisScript.of(script, String::class.java)
    }
}