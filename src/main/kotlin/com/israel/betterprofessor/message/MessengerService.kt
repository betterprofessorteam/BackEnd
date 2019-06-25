package com.israel.betterprofessor.message

import com.israel.betterprofessor.model.Message
import com.israel.betterprofessor.repository.MessageRepository
import com.israel.betterprofessor.service.MessageService
import com.israel.betterprofessor.service.TrackerService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class MessengerService(
        private val trackerService: TrackerService,
        private val messageRepository: MessageRepository
) {

    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    fun sendMessage() {
        val trackers = trackerService.findAllLiveTracker()
        val currentTime = System.currentTimeMillis()
        trackers.forEach {
            // TODO check if the users still exists

            val newMessage = Message(it.messageReceiverUserId, it.messageSenderUserId, currentTime, it.messageText)

            messageRepository.save(newMessage)

            it.shouldSendMessage = false
        }

        trackerService.saveAll(trackers)
    }

}