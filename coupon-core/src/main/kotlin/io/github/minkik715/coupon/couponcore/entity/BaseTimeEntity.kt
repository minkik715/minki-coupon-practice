package io.github.minkik715.coupon.couponcore.entity

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseTimeEntity {

    @CreatedDate
    private var dateCreated: LocalDateTime = LocalDateTime.now();

    @LastModifiedDate
    private var dateUpdated: LocalDateTime = LocalDateTime.now();
}