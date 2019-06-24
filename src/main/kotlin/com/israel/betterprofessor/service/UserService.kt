package com.israel.betterprofessor.service

import com.israel.betterprofessor.model.Mentor
import com.israel.betterprofessor.model.Student
import com.israel.betterprofessor.model.User

interface UserService {

    fun findCurrentUser(): User
    fun findCurrentMentor(): Mentor
    fun findCurrentStudent(): Student
    fun findAllStudentUser(): MutableList<User>
    fun findAll(): List<User>
    fun findUserById(id: Long): User
    fun delete(id: Long)
    fun save(user: User): User
    fun update(user: User, id: Long): User

    fun addStudentToMentor(id: Long)
}