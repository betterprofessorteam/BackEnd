package com.israel.betterprofessor.model

import io.swagger.annotations.ApiModelProperty
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
    @ApiModelProperty(hidden = true)
    @CreatedBy
    protected var createdBy: String? = null

    @ApiModelProperty(hidden = true)
    @CreatedDate
    @Temporal(TIMESTAMP)
    protected var createdDate: Date? = null

    @ApiModelProperty(hidden = true)
    @LastModifiedBy
    protected var lastModifiedBy: String? = null

    @ApiModelProperty(hidden = true)
    @LastModifiedDate
    @Temporal(TIMESTAMP)
    protected var lastModifiedDate: Date? = null
}
