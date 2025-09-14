package io.github.minkik715.coupon.couponcore.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class WebConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper{
        return jacksonObjectMapper().registerKotlinModule()
    }

}