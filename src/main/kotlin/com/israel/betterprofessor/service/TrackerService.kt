package com.israel.betterprofessor.service

import com.israel.betterprofessor.model.Tracker

interface TrackerService {

    fun findAll(): MutableList<Tracker>
    fun findAllLiveTracker(): MutableList<Tracker>
    fun save(tracker: Tracker): Tracker
    fun saveAll(trackers: MutableList<Tracker>)
}