package com.israel.betterprofessor.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
class JsonFieldNotFoundException(field: String) : RuntimeException("Json field \"$field\" not found!")