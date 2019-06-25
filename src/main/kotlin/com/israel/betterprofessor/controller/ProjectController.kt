package com.israel.betterprofessor.controller

import com.israel.betterprofessor.model.Project
import com.israel.betterprofessor.service.ProjectService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class ProjectController(private val projectService: ProjectService) {

    @ApiOperation(value = "Add a project to the current student user", response = Project::class)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/projects")
    fun addProject(@RequestBody project: Project): ResponseEntity<*> {
        return ResponseEntity(projectService.save(project), HttpStatus.CREATED)
    }

    @ApiOperation(value = "Get all projects of the current student user", response = Project::class, responseContainer = "List")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/projects")
    fun getProjects(): ResponseEntity<*> {
        return ResponseEntity(projectService.findAllOfCurrent(), HttpStatus.OK)
    }

    @ApiOperation(value = "Get all projects of the specified student user", response = Project::class, responseContainer = "List")
    @PreAuthorize("hasAuthority('ROLE_MENTOR')")
    @GetMapping("/projects/student/{id}")
    fun getProjects(@PathVariable("id") id: Long): ResponseEntity<*> {
        return ResponseEntity(projectService.findAll(id), HttpStatus.OK)
    }

}