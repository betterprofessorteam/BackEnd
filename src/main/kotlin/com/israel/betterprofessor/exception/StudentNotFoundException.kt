package com.israel.betterprofessor.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class StudentNotFoundException(id: Long) : RuntimeException("Student id $id cannot be found!")