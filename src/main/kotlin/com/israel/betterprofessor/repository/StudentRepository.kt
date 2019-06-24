package com.israel.betterprofessor.repository

import com.israel.betterprofessor.model.Student
import org.springframework.data.repository.CrudRepository

interface StudentRepository : CrudRepository<Student, Long>