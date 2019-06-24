package com.israel.betterprofessor.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class RoleNotFoundException(id: Long) : ResourceNotFoundException("Role id $id cannot be found!")