package com.israel.betterprofessor.message

import com.israel.betterprofessor.service.TrackerService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class MessengerService(private val trackerService: TrackerService) {

    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    fun sendMessage() {
        val trackers = trackerService.findAllLiveTracker()
        trackers.forEach {

            // TODO can only send message to own student. How about no

            it.shouldSendMessage = false
        }

        trackerService.saveAll(trackers)
    }

}