package io.github.minkik715.coupon.couponcore.configuration

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedisConfiguration {
    @Value("\${spring.data.redis.host}")
    lateinit var host: String

    @Value("\${spring.data.redis.port}")
    var port: Int = 6380

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        val redisAddress = "redis://$host:$port"
        config.useSingleServer().address = redisAddress
        return Redisson.create(config)
    }

}