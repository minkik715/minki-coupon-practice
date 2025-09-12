package io.github.minkik715.coupon.couponcore.component

import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class DistributeLockExecutor(
    private val redissonClient: RedissonClient
) {

    private val log = LoggerFactory.getLogger(DistributeLockExecutor::class.java)

    fun execute(
        lockName: String,
        logic: Runnable,
        waitMilliseconds: Long = 10000L,
        leasMilliseconds:Long = 10000L
    ){
        val lock = redissonClient.getLock(lockName)
        val isLocked = lock.tryLock(waitMilliseconds, leasMilliseconds, TimeUnit.MILLISECONDS)
        try{
            if(!isLocked){
                throw IllegalArgumentException("[$lockName] Lock 획득 실패")
            }
            logic.run()
        }catch (e: Exception){
            log.error(e.message, e)
            throw RuntimeException(e)
        } finally {
            if(lock.isHeldByCurrentThread){
                lock.unlock()
            }
        }


    }

}