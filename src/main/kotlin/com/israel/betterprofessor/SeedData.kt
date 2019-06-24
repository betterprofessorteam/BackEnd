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

        roleRepository.save(adminRole)
        roleRepository.save(userRole)

        val adminUserRoles = mutableListOf<UserRole>()
        adminUserRoles.add(UserRole(User(), adminRole))
        adminUserRoles.add(UserRole(User(), userRole))
        val adminUser = User("admin", "password", adminUserRoles)

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

        val faker = Faker()

        for (i in 0..10) {
            val userUserRoles = mutableListOf<UserRole>()
            userUserRoles.add(UserRole(User(), userRole))

            val mentorUser = User("mentor$i", "password", userUserRoles)

            val mentor = Mentor(faker.name().firstName(), faker.name().lastName())

            mentorUser.mentorData = mentor
            mentor.user = mentorUser

            userRepository.save(mentorUser)
        }

        for (i in 0..100) {
            val userUserRoles = mutableListOf<UserRole>()
            userUserRoles.add(UserRole(User(), userRole))

            val studentUser = User("student$i", "password", userUserRoles)

            val student = Student(faker.name().firstName(), faker.name().lastName())

            studentUser.studentData = student
            student.user = studentUser

            userRepository.save(studentUser)
        }
    }
}