package com.israel.betterprofessor.repository

import com.israel.betterprofessor.model.Mentor
import org.springframework.data.repository.CrudRepository

interface MentorRepository : CrudRepository<Mentor, Long>