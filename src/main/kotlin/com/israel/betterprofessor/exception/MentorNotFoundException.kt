package com.israel.betterprofessor.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class MentorNotFoundException(id: Long) : RuntimeException("Mentor id $id cannot be found!")