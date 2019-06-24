package com.israel.betterprofessor.exception

import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus
class UserNotFoundException : RuntimeException {

    constructor(id: Long) : super("User id $id cannot be found!")
    constructor(username: String) : super("User username $username cannot be found!")

}