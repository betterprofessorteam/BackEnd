package com.israel.betterprofessor.exception

class JsonFieldNotFoundException(field: String) : BadRequestException("Json field \"$field\" not found!")