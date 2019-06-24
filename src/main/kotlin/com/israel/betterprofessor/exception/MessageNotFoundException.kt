package com.israel.betterprofessor.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class MessageNotFoundException : ResourceNotFoundException {
    constructor(id: Long): super("Message id $id cannot be found!")
}