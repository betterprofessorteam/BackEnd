package com.israel.betterprofessor.service

import com.israel.betterprofessor.StaticHelpers
import com.israel.betterprofessor.exception.BadRequestException
import com.israel.betterprofessor.model.Tracker
import com.israel.betterprofessor.repository.TrackerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service(value = "trackerService")
class TrackerServiceImpl(
        private val trackerRepository: TrackerRepository,
        private val userService: UserService
) : TrackerService {

    @Transactional
    override fun saveAll(trackers: MutableList<Tracker>) {
        trackerRepository.saveAll(trackers)
    }

    override fun findAll(): MutableList<Tracker> {
        val trackers = mutableListOf<Tracker>()
        trackerRepository.findAll().iterator().forEachRemaining { trackers.add(it) }
        return trackers
    }

    override fun findAllLiveTracker(): MutableList<Tracker> {
        val trackers = mutableListOf<Tracker>()
        val currentTime = System.currentTimeMillis()
        trackerRepository.findAllLiveTracker(currentTime).iterator().forEachRemaining { trackers.add(it) }
        return trackers
    }

    @Transactional
    override fun save(tracker: Tracker): Tracker {
        StaticHelpers.checkJsonField(tracker.messageToUserId, "messageToUserId")
        StaticHelpers.checkJsonField(tracker.type, "type")
        StaticHelpers.checkJsonField(tracker.name, "name")
        StaticHelpers.checkJsonField(tracker.deadline, "deadline")
        StaticHelpers.checkJsonField(tracker.shouldSendMessage, "shouldSendMessage")
        StaticHelpers.checkJsonField(tracker.messageText, "messageText")

        if (!Tracker.isValidType(tracker.type!!)) throw BadRequestException("Invalid tracker type")
        val currentUser = userService.findCurrentUser()
        userService.findUserById(tracker.messageToUserId!!)

        var newTracker = Tracker(
                tracker.type,
                tracker.name,
                tracker.deadline,
                tracker.shouldSendMessage,
                currentUser.userId,
                tracker.messageToUserId,
                tracker.messageText
        )

        return trackerRepository.save(newTracker)
    }

}