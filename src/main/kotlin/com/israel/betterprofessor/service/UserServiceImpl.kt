package com.israel.betterprofessor.service

import com.israel.betterprofessor.StaticHelpers
import com.israel.betterprofessor.exception.BadRequestException
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

import java.util.ArrayList
import java.util.regex.Pattern

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
        return currentUser.mentorData ?: throw BadRequestException("Current user is not a mentor")
    }

    override fun findCurrentStudent(): Student {
        val currentUser = findCurrentUser()
        return currentUser.studentData ?: throw BadRequestException("Current user is not a student")
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
            throw UserNotFoundException(id)
        }
    }

    override fun delete() {
        val currentUser = findCurrentUser()

        delete(currentUser.userId!!)
    }

    @Transactional
    override fun save(user: User): User {
        val username = StaticHelpers.checkJsonField(user.username, "username")
        if (!isValidUsername(username)) throw BadRequestException("Invalid username!")

        val password = StaticHelpers.checkJsonField(user.password, "password")
        if (!isValidPassword(password)) throw BadRequestException("Invalid password!")

        if (userRepository.findByUsername(username) != null) throw BadRequestException("Username already taken!!!")

        val newUser = User()
        newUser.username = username
        newUser.setPasswordEncrypt(password)
        newUser.email = user.email

        // mentor/student data
        when {
            user.mentorData != null -> {
                newUser.mentorData = Mentor(
                        StaticHelpers.checkJsonField(user.mentorData!!.firstName, "firstName"),
                        StaticHelpers.checkJsonField(user.mentorData!!.lastName, "lastName")
                )
                newUser.mentorData!!.user = newUser

                val mentorRole = roleRepository.findByName("mentor")
                newUser.userRoles.add(UserRole(newUser, mentorRole))
            }
            user.studentData != null -> {
                newUser.studentData = Student(
                        StaticHelpers.checkJsonField(user.studentData!!.firstName, "firstName"),
                        StaticHelpers.checkJsonField(user.studentData!!.lastName, "lastName")
                )
                newUser.studentData!!.user = newUser

                val studentRole = roleRepository.findByName("student")
                newUser.userRoles.add(UserRole(newUser, studentRole))
            }
            else -> throw BadRequestException("No user type found")
        }

        val userRole = roleRepository.findByName("user")

        newUser.userRoles.add(UserRole(newUser, userRole))

        return userRepository.save(newUser)
    }

    @Transactional
    override fun update(user: User): User {
        val currentUser = findCurrentUser()

        if (user.username != null) currentUser.username = user.username
        if (user.password != null) currentUser.setPasswordEncrypt(user.password!!)
        if (user.email != null) currentUser.email = user.email

        // @NOTE: roles cannot be updated

        return userRepository.save(currentUser)
    }

    @Transactional
    override fun addStudentToMentor(id: Long) {
        val studentUser = findUserById(id)

        studentUser.studentData ?: throw BadRequestException("User id $id is not a student")

        val currentMentorUser = findCurrentMentor().also {
            if (it.students.contains(studentUser.studentData!!)) throw BadRequestException("User id $id already added to mentor's students")
            it.students.add(studentUser.studentData!!)
        }.user

        userRepository.save(currentMentorUser!!)

    }

    @Transactional
    override fun removeStudentFromMentor(id: Long) {
        val studentUser = findUserById(id)

        studentUser.studentData ?: throw BadRequestException("User id $id is not a student")

        val currentMentorUser = findCurrentMentor().also {
            if (!it.students.contains(studentUser.studentData!!)) throw BadRequestException("User id $id is not added to mentor's students")
            it.students.remove(studentUser.studentData!!)
        }.user

        userRepository.save(currentMentorUser!!)
    }

    fun isValidUsername(username: String): Boolean {
        if (username.length < 6) return false

        val pattern = Pattern.compile("^[a-zA-Z0-9]+\$")
        return pattern.matcher(username).matches()
    }

    fun isValidPassword(password: String): Boolean {
        if (password.length < 6) return false

        val pattern = Pattern.compile("^[a-zA-Z0-9]+\$")
        return pattern.matcher(password).matches()
    }
}
