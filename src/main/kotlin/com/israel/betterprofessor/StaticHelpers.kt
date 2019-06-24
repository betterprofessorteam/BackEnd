package com.israel.betterprofessor

import com.israel.betterprofessor.exception.JsonFieldNotFoundException

class StaticHelpers {

    companion object {
        fun <T: Any?> checkJsonField(field: T, fieldName: String) {
            if (field == null) throw JsonFieldNotFoundException(fieldName)
        }
    }

}