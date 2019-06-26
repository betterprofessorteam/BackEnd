package com.israel.betterprofessor.controller

import com.israel.betterprofessor.model.Tracker
import com.israel.betterprofessor.service.TrackerService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TrackerController(private val trackerService: TrackerService) {

    @ApiOperation(value = "Add a tracker for the current mentor user", response = Tracker::class)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/trackers")
    fun addTracker(@RequestBody tracker: Tracker): ResponseEntity<*> {
        return ResponseEntity(trackerService.save(tracker), HttpStatus.CREATED)
    }

    @ApiOperation(value = "Get trackers of the current mentor user", response = Tracker::class, responseContainer = "List")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/trackers")
    fun getTrackers(): ResponseEntity<*> {
        return ResponseEntity(trackerService.findAllOfCurrent(), HttpStatus.OK)
    }

}