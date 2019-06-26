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

    override fun findAllOfCurrent(): MutableList<Tracker> {
        val currentUser = userService.findCurrentUser()

        if (currentUser.mentorData == null) throw BadRequestException("Current user is not a mentor")

        return currentUser.mentorData!!.trackers
    }

    override fun findAllLiveTracker(): MutableList<Tracker> {
        val trackers = mutableListOf<Tracker>()
        trackerRepository.findAllTrackerToSend().iterator().forEachRemaining { trackers.add(it) }
        return trackers
    }

    @Transactional
    override fun save(tracker: Tracker): Tracker {
        // TODO can only send message to own student. How about no

        StaticHelpers.checkJsonField(tracker.messageReceiverUserId, "messageReceiverUserId")
        StaticHelpers.checkJsonField(tracker.type, "type")
        StaticHelpers.checkJsonField(tracker.name, "name")
        StaticHelpers.checkJsonField(tracker.deadline, "deadline")
        StaticHelpers.checkJsonField(tracker.shouldSendMessage, "shouldSendMessage")
        StaticHelpers.checkJsonField(tracker.messageText, "messageText")

        if (!Tracker.isValidType(tracker.type!!)) throw BadRequestException("Invalid tracker type")
        val currentUser = userService.findCurrentUser()
        userService.findUserById(tracker.messageReceiverUserId!!)

        currentUser.mentorData ?: throw BadRequestException("Current user must be a mentor")

        val newTracker = Tracker(
                tracker.type,
                tracker.name,
                tracker.deadline,
                tracker.shouldSendMessage,
                currentUser.userId,
                tracker.messageReceiverUserId,
                tracker.messageText,
                currentUser.mentorData
        )

        return trackerRepository.save(newTracker)
    }

}