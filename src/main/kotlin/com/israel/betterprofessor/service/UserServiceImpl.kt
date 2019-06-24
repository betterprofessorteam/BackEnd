package com.israel.betterprofessor.service

import com.israel.betterprofessor.exception.JsonFieldNotFoundException
import com.israel.betterprofessor.exception.UserNotFoundException
import com.israel.betterprofessor.model.Mentor
import com.israel.betterprofessor.model.Student
import com.israel.betterprofessor.model.User
import com.israel.betterprofessor.model.UserRole
import com.israel.betterprofessor.repository.RoleRepository
import com.israel.betterprofessor.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException

import javax.persistence.EntityNotFoundException
import java.util.ArrayList

@Service(value = "userService")
open class UserServiceImpl(
        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository
) : UserDetailsService, UserService {

    override fun findCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUser = userRepository.findByUsername(authentication.name)
        return currentUser ?: throw UserNotFoundException(authentication.name)
    }

    override fun findCurrentMentor(): Mentor {
        val currentUser = findCurrentUser()
        return currentUser.mentorData ?: throw RuntimeException("Current user is not a mentor")
    }

    override fun findCurrentStudent(): Student {
        val currentUser = findCurrentUser()
        return currentUser.studentData ?: throw RuntimeException("Current user is not a student")
    }

    override fun findAllStudentUser(): MutableList<User> {
        return userRepository.findAllStudentUser()
    }

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
                ?: throw UsernameNotFoundException("Invalid username or password.")
        return org.springframework.security.core.userdetails.User(user.username, user.password, user.getAuthority())
    }

    override fun findUserById(id: Long): User {
        return userRepository.findById(id)
                .orElseThrow { UserNotFoundException(id) }
    }

    override fun findAll(): List<User> {
        val list = ArrayList<User>()
        userRepository.findAll().iterator().forEachRemaining { list.add(it) }
        return list
    }

    override fun delete(id: Long) {
        if (userRepository.findById(id).isPresent) {
            userRepository.deleteById(id)
        } else {
            throw EntityNotFoundException(id.toString())
        }
    }

    @Transactional
    override fun save(user: User): User {
        val newUser = User()
        newUser.username = user.username ?: throw JsonFieldNotFoundException("username")
        newUser.setPasswordEncrypt(user.password ?: throw JsonFieldNotFoundException("password"))

        when {
            user.mentorData != null -> {
                user.mentorData!!.firstName ?: throw JsonFieldNotFoundException("firstName")
                user.mentorData!!.lastName ?: throw JsonFieldNotFoundException("lastName")
                newUser.mentorData = user.mentorData
                newUser.mentorData!!.user = newUser
            }
            user.studentData != null -> {
                user.studentData!!.firstName ?: throw JsonFieldNotFoundException("firstName")
                user.studentData!!.lastName ?: throw JsonFieldNotFoundException("lastName")
                newUser.studentData = user.studentData
                newUser.studentData!!.user = newUser
            }
            else -> throw RuntimeException("No user type found")
        }

        val userRole = roleRepository.findByName("user")

        newUser.userRoles = mutableListOf()
        newUser.userRoles.add(UserRole(newUser, userRole))

        return userRepository.save(newUser)
    }

    @Transactional
    override fun update(user: User, id: Long): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUser = userRepository.findByUsername(authentication.name)

        if (currentUser != null) {
            if (id == currentUser.userId) {

                if (user.username != null) {
                    currentUser.username = user.username
                }

                if (user.password != null) {
                    currentUser.setPasswordEncrypt(user.password!!)
                }

                if (user.userRoles.isNotEmpty()) {
                    // with so many relationships happening, I decided to go
                    // with old school queries
                    // delete the old ones
                    roleRepository.deleteAllUserRoleByUserId(currentUser.userId!!)

                    // add the new ones
                    for (ur in user.userRoles) {
                        roleRepository.insertUserRole(id, ur.role!!.roleId)
                    }
                }

                return userRepository.save(currentUser)
            } else {
                throw RuntimeException("Current user invalid userId")
            }
        } else {
            throw UserNotFoundException(authentication.name)
        }
    }

    @Transactional
    override fun addStudentToMentor(id: Long) {
        val studentUser = findUserById(id)

        studentUser.studentData ?: throw RuntimeException("User id $id is not a student")

        val currentMentorUser = findCurrentMentor().also {
            if (it.students.contains(studentUser.studentData!!)) throw RuntimeException("User id $id already added to mentor's students")
            it.students.add(studentUser.studentData!!)
        }.user

        userRepository.save(currentMentorUser!!)

    }
}
