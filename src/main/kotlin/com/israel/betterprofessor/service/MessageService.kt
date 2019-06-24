package com.israel.betterprofessor.service

import com.israel.betterprofessor.model.Message

interface MessageService {

    fun sendMessage(message: Message): Message
    fun findAllMessage(): MutableList<Message>
    fun findAllMessageWithUser(id: Long): MutableList<Message>
    fun setMessageRead(id: Long)

}