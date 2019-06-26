package com.israel.betterprofessor

import com.github.javafaker.Faker
import com.israel.betterprofessor.model.*
import com.israel.betterprofessor.repository.RoleRepository
import com.israel.betterprofessor.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional
@Component
class SeedData(
        private val roleRepository: RoleRepository,
        private val userRepository: UserRepository
): CommandLineRunner {
    override fun run(vararg args: String?) {
        val adminRole = Role("admin")
        val userRole = Role("user")
        val mentorRole = Role("mentor")
        val studentRole = Role("student")

        roleRepository.save(adminRole)
        roleRepository.save(userRole)
        roleRepository.save(mentorRole)
        roleRepository.save(studentRole)

        val adminUserRoles = mutableListOf<UserRole>()
        adminUserRoles.add(UserRole(User(), adminRole))
        adminUserRoles.add(UserRole(User(), userRole))
        val adminUser = User("admin", "password", adminUserRoles)
        adminUser.email = "admin@betterprofessor.com"

        userRepository.save(adminUser)

//        val userUserRoles0 = mutableListOf<UserRole>()
//        userUserRoles0.add(UserRole(User(), userRole))
//
//        val student0 = Student("Stu", "Dent")
//
//        val studentUser0 = User("student", "password", userUserRoles0)
//        studentUser0.studentData = student0
//        student0.user = studentUser0
//
//        userRepository.save(studentUser0)
//
        val faker = Faker()

        for (i in 0..9) {
            val mentorUserRoles = mutableListOf<UserRole>()
            mentorUserRoles.add(UserRole(User(), userRole))
            mentorUserRoles.add(UserRole(User(), mentorRole))

            val mentorUser = User("mentor$i", "password", mentorUserRoles)

            val mentor = Mentor(faker.name().firstName(), faker.name().lastName())
            mentorUser.email = mentor.firstName!!.toLowerCase() + mentor.lastName!![0].toLowerCase() + i + "@betterprofessor.com"

            mentorUser.mentorData = mentor
            mentor.user = mentorUser

            userRepository.save(mentorUser)
        }

        for (i in 0..99) {
            val studentUserRoles = mutableListOf<UserRole>()
            studentUserRoles.add(UserRole(User(), userRole))
            studentUserRoles.add(UserRole(User(), studentRole))

            val studentUser = User("student$i", "password", studentUserRoles)

            val student = Student(faker.name().firstName(), faker.name().lastName())
            studentUser.email = student.firstName!!.toLowerCase() + student.lastName!![0].toLowerCase() + i + "@betterprofessor.com"

            studentUser.studentData = student
            student.user = studentUser

            userRepository.save(studentUser)
        }
    }
}