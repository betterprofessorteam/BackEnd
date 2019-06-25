package com.israel.betterprofessor.service

import com.israel.betterprofessor.StaticHelpers
import com.israel.betterprofessor.exception.BadRequestException
import com.israel.betterprofessor.model.Project
import com.israel.betterprofessor.repository.ProjectRepository
import org.springframework.stereotype.Service

@Service("projectService")
class ProjectServiceImpl(
        private val projectRepository: ProjectRepository,
        private val userService: UserService
) : ProjectService {

    override fun save(project: Project): Project {
        StaticHelpers.checkJsonField(project.title, "title")
        StaticHelpers.checkJsonField(project.deadline, "deadline")

        val currentUser = userService.findCurrentUser()

        if (currentUser.studentData == null) throw BadRequestException("Current user is not a student")

        val newProject = Project(project.title, project.deadline, currentUser.studentData)

        return projectRepository.save(newProject)
    }

}