package com.israel.betterprofessor.exception

import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus
class UserNotFoundException : ResourceNotFoundException {

    constructor(id: Long) : super("User id $id cannot be found!")
    constructor(username: String) : super("User username $username cannot be found!")

}