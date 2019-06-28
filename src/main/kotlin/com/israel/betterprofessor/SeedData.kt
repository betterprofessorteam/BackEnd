package com.israel.betterprofessor

import com.github.javafaker.Faker
import com.israel.betterprofessor.model.*
import com.israel.betterprofessor.repository.*
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@Transactional
@Component
class SeedData(
        private val roleRepository: RoleRepository,
        private val userRepository: UserRepository,
        private val mentorRepository: MentorRepository,
        private val studentRepository: StudentRepository
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

        val savedMentorUsers = mutableListOf<User>()
        for (i in 0..9) {
            val mentorUserRoles = mutableListOf<UserRole>()
            mentorUserRoles.add(UserRole(User(), userRole))
            mentorUserRoles.add(UserRole(User(), mentorRole))

            val mentorUser = User("mentor$i", "password", mentorUserRoles)

            val mentor = Mentor(faker.name().firstName(), faker.name().lastName())
            mentorUser.email = mentor.firstName!!.toLowerCase() + mentor.lastName!![0].toLowerCase() + i + "@betterprofessor.com"

            mentorUser.mentorData = mentor
            mentor.user = mentorUser

            val savedMentorUser = userRepository.save(mentorUser)
            savedMentorUsers.add(savedMentorUser)
        }

        val random = Random()
        val longStream = random.longs(3600000, 15770000000) // 1 hour to 6 months

        val STUDENT_PROJECT_SIZE = 10

        val currentTime = System.currentTimeMillis()
        val savedStudentUsers = mutableListOf<User>()
        for (i in 0..99) {
            val studentUserRoles = mutableListOf<UserRole>()
            studentUserRoles.add(UserRole(User(), userRole))
            studentUserRoles.add(UserRole(User(), studentRole))

            val studentUser = User("student$i", "password", studentUserRoles)

            val student = Student(faker.name().firstName(), faker.name().lastName())
            studentUser.email = student.firstName!!.toLowerCase() + student.lastName!![0].toLowerCase() + i + "@betterprofessor.com"

            studentUser.studentData = student
            student.user = studentUser

            for (j in 0 until STUDENT_PROJECT_SIZE) {
                val project = Project()
                project.title = "Project$j"
                project.deadline = ThreadLocalRandom.current().nextLong(currentTime + 3600000, currentTime + 15770000000) // 1 hour to 6 months
                project.student = student
                student.projects.add(project)
            }

            val savedStudentUser = userRepository.save(studentUser)
            savedStudentUsers.add(savedStudentUser)
        }

        val MENTOR_STUDENT_SIZE = 5
        for (savedMentorUser in savedMentorUsers) {
            val mentor = savedMentorUser.mentorData!!
            val studentUserIds = mutableListOf<Long>()
            if (savedStudentUsers.size < 5) throw RuntimeException()
            for (i in 0 until MENTOR_STUDENT_SIZE) {
                var studentUserId: Long
                while (true) {
                    val studentUserIndex = ThreadLocalRandom.current().nextInt(0, (savedStudentUsers.size - 1))
                    studentUserId = savedStudentUsers[studentUserIndex].userId!!
                    if (!studentUserIds.contains(studentUserId)) break // not added yet
                }

                // add student to mentor
                studentUserIds.add(studentUserId)
                val savedStudentUser = savedStudentUsers.find { it.userId == studentUserId }
                val student = savedStudentUser!!.studentData!!
                student.mentors.add(mentor)
                mentor.students.add(student)

                // add 1 tracker for a project from this student
                val projectIdIndex = ThreadLocalRandom.current().nextInt(0, (student.projects.size - 1))
                val project = student.projects[projectIdIndex]
                val tracker = Tracker()
                tracker.type = Tracker.TYPE_PROJECT
                tracker.name = "Reminder for ${student.firstName} ${student.lastName}'s ${project.title} project"
                tracker.deadline = project.deadline
                tracker.shouldSendMessage = true
                tracker.messageSenderUserId = savedMentorUser.userId
                tracker.messageReceiverUserId = savedMentorUser.userId
                tracker.messageTitle = ""
                tracker.messageText = ""
                tracker.mentor = mentor
                mentor.trackers.add(tracker)

                studentRepository.save(student)
            }

            val messageSentToStudentSize = ThreadLocalRandom.current().nextInt(5, 200)
            for (i in 0 until messageSentToStudentSize) {
                val studentUserIndex = ThreadLocalRandom.current().nextInt(0, (savedStudentUsers.size - 1))
                val studentUserId = savedStudentUsers[studentUserIndex].userId!!

                savedMentorUser.messageSent.add(createMessage(savedMentorUser.userId!!, studentUserId, faker, currentTime))
            }

            userRepository.save(savedMentorUser)
        }

        for (savedStudentUser in savedStudentUsers) {
            val messageSentToMentorSize = ThreadLocalRandom.current().nextInt(5, 20)
            for (i in 0 until messageSentToMentorSize) {
                val mentorUserIndex = ThreadLocalRandom.current().nextInt(0, (savedMentorUsers.size - 1))
                val mentorUserId = savedMentorUsers[mentorUserIndex].userId!!

                savedStudentUser.messageSent.add(createMessage(savedStudentUser.userId!!, mentorUserId, faker, currentTime))
            }

            userRepository.save(savedStudentUser)
        }
    }

    fun createMessage(senderUserId: Long, receiverUserId: Long, faker: Faker, currentTime: Long): Message {
        return Message().also {
            it.senderUserId = senderUserId
            it.receiverUserId = receiverUserId

            it.timeSent = ThreadLocalRandom.current().nextLong(currentTime - 15770000000, currentTime)
            it.timeRead = if (ThreadLocalRandom.current().nextBoolean()) {
                ThreadLocalRandom.current().nextLong(it.timeSent!!, currentTime)
            } else {
                0L
            }

            it.title = faker.pokemon().name()
            it.text = faker.shakespeare().romeoAndJulietQuote()
        }
    }
}