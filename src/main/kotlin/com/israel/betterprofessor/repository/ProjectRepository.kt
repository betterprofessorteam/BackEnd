package com.israel.betterprofessor.repository

import com.israel.betterprofessor.model.Project
import org.springframework.data.repository.CrudRepository

interface ProjectRepository : CrudRepository<Project, Long>