package com.israel.betterprofessor.handler

import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap

// adapted from https://tools.ietf.org/html/rfc7807
class ErrorDetail {
    var title: String? = null
    var status: Int = 0
    var detail: String? = null
    var timestamp: String? = null
        private set
    var developerMessage: String? = null
    var errors: Map<String, List<ValidationError>> = HashMap()

    fun setTimestamp(timestamp: Long?) {
        this.timestamp = SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(Date(timestamp!!))
    }
}
