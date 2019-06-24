package com.israel.betterprofessor.service

import com.israel.betterprofessor.model.Tracker
import com.israel.betterprofessor.repository.TrackerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service(value = "trackerService")
class TrackerServiceImpl(
        private val trackerRepository: TrackerRepository
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
    override fun save(tracker: Tracker): Long {
        return trackerRepository.save(tracker).trackerId!!
    }

}