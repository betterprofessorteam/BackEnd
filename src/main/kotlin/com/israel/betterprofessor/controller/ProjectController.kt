package com.israel.betterprofessor.controller

import com.israel.betterprofessor.model.Message
import com.israel.betterprofessor.model.Project
import com.israel.betterprofessor.service.ProjectService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProjectController(private val projectService: ProjectService) {

    @ApiOperation(value = "Add a project to the current user", response = Message::class)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/projects")
    fun addProject(@RequestBody project: Project): ResponseEntity<*> {
        return ResponseEntity(projectService.save(project), HttpStatus.CREATED)
    }

}