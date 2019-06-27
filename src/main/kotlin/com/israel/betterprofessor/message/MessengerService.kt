package com.israel.betterprofessor.message

import com.israel.betterprofessor.exception.UserNotFoundException
import com.israel.betterprofessor.model.Message
import com.israel.betterprofessor.repository.MessageRepository
import com.israel.betterprofessor.service.MessageService
import com.israel.betterprofessor.service.TrackerService
import com.israel.betterprofessor.service.UserService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class MessengerService(
        private val trackerService: TrackerService,
        private val messageRepository: MessageRepository,
        private val userService: UserService
) {

    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    fun sendMessage() {
        val trackers = trackerService.findAllLiveTracker()
        val currentTime = System.currentTimeMillis()
        trackers.forEach {
            try {
                // check if the users still exists
                userService.findUserById(it.messageSenderUserId!!)
                userService.findUserById(it.messageReceiverUserId!!)

                val newMessage = Message(it.messageReceiverUserId, it.messageSenderUserId, currentTime, it.messageTitle, it.messageText)

                messageRepository.save(newMessage)
            } catch (e: UserNotFoundException) {
                // TODO log
            }

            it.shouldSendMessage = false
        }

        trackerService.saveAll(trackers)
    }

}