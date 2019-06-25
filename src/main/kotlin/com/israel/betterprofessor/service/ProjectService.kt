package com.israel.betterprofessor.service

import com.israel.betterprofessor.model.Project

interface ProjectService {

    fun save(project: Project): Project
    fun findAllOfCurrent(): MutableList<Project>
    fun findAll(id: Long): MutableList<Project>

}