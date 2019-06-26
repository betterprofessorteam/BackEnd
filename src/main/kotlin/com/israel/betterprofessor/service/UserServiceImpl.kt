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

    companion object {
        const val VALID_USERNAME_REGEX = "^[a-zA-Z0-9]+\$"
        const val VALID_PASSWORD_REGEX = "^[a-zA-Z0-9]+\$"
        const val VALID_EMAIL_REGEX = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

        val VALID_USERNAME_PATTERN = Pattern.compile(VALID_USERNAME_REGEX)
        val VALID_PASSWORD_PATTERN = Pattern.compile(VALID_PASSWORD_REGEX)
        val VALID_EMAIL_PATTERN = Pattern.compile(VALID_EMAIL_REGEX)

    }

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

        val email = StaticHelpers.checkJsonField(user.email, "email")
        if (!isValidEmail(email)) throw BadRequestException("Invalid email!")

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
                newUser.userRoles.add(UserRole(newUser, mentorRole!!))
            }
            user.studentData != null -> {
                newUser.studentData = Student(
                        StaticHelpers.checkJsonField(user.studentData!!.firstName, "firstName"),
                        StaticHelpers.checkJsonField(user.studentData!!.lastName, "lastName")
                )
                newUser.studentData!!.user = newUser

                val studentRole = roleRepository.findByName("student")
                newUser.userRoles.add(UserRole(newUser, studentRole!!))
            }
            else -> throw BadRequestException("No user type found")
        }

        val userRole = roleRepository.findByName("user")

        newUser.userRoles.add(UserRole(newUser, userRole!!))

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

        return VALID_USERNAME_PATTERN.matcher(username).matches()
    }

    fun isValidPassword(password: String): Boolean {
        if (password.length < 6) return false

        return VALID_PASSWORD_PATTERN.matcher(password).matches()
    }

    fun isValidEmail(email: String): Boolean {
        if (email.isEmpty()) return false

        return VALID_EMAIL_PATTERN.matcher(email).matches()
    }
}
