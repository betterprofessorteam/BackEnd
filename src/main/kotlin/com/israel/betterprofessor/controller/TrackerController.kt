package com.israel.betterprofessor.controller

import com.israel.betterprofessor.model.Tracker
import com.israel.betterprofessor.model.User
import com.israel.betterprofessor.service.TrackerService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TrackerController(private val trackerService: TrackerService) {

    @ApiOperation(value = "Add a tracker for the current user",responseReference = """{"id":1}""")
    @PostMapping("/trackers")
    fun addTracker(@RequestBody tracker: Tracker): ResponseEntity<*> {
        val id = trackerService.save(tracker)
        return ResponseEntity("""{"id":$id}""", HttpStatus.CREATED)
    }

}