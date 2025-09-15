package io.github.minkik715.coupon.couponcore.exception

enum class ErrorCode(var msg: String) {
    INVALID_COUPON_ISSUE_QUANTITY("발급 가능한 수량을 유효하지 않은 쿠폰입니다.."),
    INVALID_COUPON_ISSUE_DATE("발급 기간이 유효하지 않은 쿠폰입니다."),
    COUPON_NOT_EXIST("존재하지 않는 쿠폰입니다."),
    COUPON_ALREADY_ISSUED("이미 발급된 쿠폰입니다."),
    COUPON_ISSUE_FAIL("쿠폰 발급에 실패했습니다."),

}
