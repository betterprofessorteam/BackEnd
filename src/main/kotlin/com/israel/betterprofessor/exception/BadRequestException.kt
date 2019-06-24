package com.israel.betterprofessor.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.BAD_REQUEST)
open class BadRequestException(details: String) : RuntimeException(details)