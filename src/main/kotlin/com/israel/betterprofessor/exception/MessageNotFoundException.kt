package com.israel.betterprofessor.exception

import java.lang.RuntimeException

class MessageNotFoundException : RuntimeException {
    constructor(id: Long): super("Message id $id cannot be found!")
}