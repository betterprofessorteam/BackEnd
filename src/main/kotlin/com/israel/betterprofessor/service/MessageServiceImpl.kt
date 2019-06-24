package com.israel.betterprofessor.service

import com.israel.betterprofessor.exception.BadRequestException
import com.israel.betterprofessor.exception.JsonFieldNotFoundException
import com.israel.betterprofessor.exception.MessageNotFoundException
import com.israel.betterprofessor.model.Message
import com.israel.betterprofessor.repository.MessageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("messageService")
class MessageServiceImpl(
        private val messageRepository: MessageRepository,
        private val userService: UserService
): MessageService {

    @Transactional
    override fun sendMessage(message: Message): Message {
        val currentUser = userService.findCurrentUser()

        if (message.receiverUserId == null) throw JsonFieldNotFoundException("receiverUserId")

        userService.findUserById(message.receiverUserId!!)

        val newMessage = Message(
                currentUser.userId,
                message.receiverUserId,
                System.currentTimeMillis(),
                message.text
        )

        return messageRepository.save(newMessage)
    }

    override fun findAllMessage(): MutableList<Message> {
        val currentUser = userService.findCurrentUser()

        return messageRepository.findAllMessageByUser(currentUser.userId!!)
    }

    override fun findAllMessageWithUser(id: Long): MutableList<Message> {
        val currentUser = userService.findCurrentUser()

        return messageRepository.findAllMessageWithUser(currentUser.userId!!, id)
    }

    @Transactional
    override fun setMessageRead(id: Long) {
        val currentUser = userService.findCurrentUser()

        val message = messageRepository.findById(id)
                .orElseThrow { MessageNotFoundException(id) }

        if (message.receiverUserId != currentUser.userId) throw BadRequestException("Message id $id was not received by the current user")

        if (message.timeRead!! > message.timeSent!!) return // message already read

        message.timeRead = System.currentTimeMillis()
        messageRepository.save(message)
    }

}