package com.israel.betterprofessor.repository

import com.israel.betterprofessor.model.Tracker
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface TrackerRepository : CrudRepository<Tracker, Long> {

    @Query(value = "SELECT * FROM trackers WHERE should_send_message=true AND deadline<:currentTime", nativeQuery = true)
    fun findAllTrackerToSend(currentTime: Long): MutableList<Tracker>

}