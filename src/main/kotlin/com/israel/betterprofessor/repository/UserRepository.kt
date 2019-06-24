package com.israel.betterprofessor.repository

import com.israel.betterprofessor.model.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository

interface UserRepository : PagingAndSortingRepository<User, Long> {
    fun findByUsername(username: String): User?

    @Query(value = "SELECT * FROM users WHERE student_data_student_id IS NOT NULL", nativeQuery =  true)
    fun findAllStudentUser(): MutableList<User>
}