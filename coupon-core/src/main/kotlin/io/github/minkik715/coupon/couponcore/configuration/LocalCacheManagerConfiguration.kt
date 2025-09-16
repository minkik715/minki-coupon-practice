package io.github.minkik715.coupon.couponcore.configuration

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit


@Configuration
class LocalCacheManagerConfiguration {

    @Bean
    fun localCacheManager(): CacheManager {
        val caffeineCacheManager = CaffeineCacheManager ()
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .maximumSize(1000))
        return caffeineCacheManager
    }
}