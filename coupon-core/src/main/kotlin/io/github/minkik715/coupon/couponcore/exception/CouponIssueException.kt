package io.github.minkik715.coupon.couponcore.exception

class CouponIssueException(
    override val message: String,
    val errorCode: ErrorCode,
) : RuntimeException(){
    override fun getLocalizedMessage(): String {
        return "[${errorCode.name}] $message"
    }
}