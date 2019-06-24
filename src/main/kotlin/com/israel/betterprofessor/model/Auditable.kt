package com.israel.betterprofessor.model

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass
import javax.persistence.Temporal
import java.util.Date

import javax.persistence.TemporalType.TIMESTAMP

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Auditable {
    @CreatedBy
    protected var createdBy: String? = null

    @CreatedDate
    @Temporal(TIMESTAMP)
    protected var createdDate: Date? = null

    @LastModifiedBy
    protected var lastModifiedBy: String? = null

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    protected var lastModifiedDate: Date? = null
}
