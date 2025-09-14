package io.github.minkik715.coupon.couponcore.repository.redis

import org.redisson.api.RedissonClient
import org.redisson.api.TransactionOptions
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class RedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
    private val redissonClient: RedissonClient
) {

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
}