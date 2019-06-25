package com.israel.betterprofessor.controller

import com.israel.betterprofessor.model.User
import com.israel.betterprofessor.service.UserService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
        private val userService: UserService
) {

    @ApiOperation(value = "Register a user", response = User::class)
    @PostMapping("/users")
    fun addUser(@RequestBody user: User): ResponseEntity<*> {
        val addedUser = userService.save(user)
        return ResponseEntity(addedUser, HttpStatus.CREATED)
    }

    @ApiOperation(value = "Get the current user data", response = User::class)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/user")
    fun getUser(): ResponseEntity<*> {
        val user = userService.findCurrentUser()
        return ResponseEntity(user, HttpStatus.OK)
    }

    @ApiOperation(value = "Get the user by its id", response = User::class)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/users/{id}")
    fun getUser(@PathVariable("id") id: Long): ResponseEntity<*> {
        val user = userService.findUserById(id)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @ApiOperation(value = "Get the current mentor's students", response = User::class, responseContainer = "List")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/user/mentor/students")
    fun getMentorStudents(): ResponseEntity<*> {
        val students = userService.findCurrentMentor().students
        val userStudents = ArrayList<User>(students.size)
        students.forEach {
            userStudents.add(it.user!!)
        }

        return ResponseEntity(userStudents, HttpStatus.OK)
    }

    @ApiOperation(value = "Get the current student's mentors", response = User::class, responseContainer = "List")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/user/student/mentors")
    fun getStudentMentors(): ResponseEntity<*> {
        val mentors = userService.findCurrentStudent().mentors
        return ResponseEntity(mentors, HttpStatus.OK)
    }

    @ApiOperation(value = "Get all users that are students", response = User::class, responseContainer = "List")
    @PreAuthorize("hasAuthority('ROLE_MENTOR')")
    @GetMapping("/user/students")
    fun getStudentUsers(): ResponseEntity<*> {
        val studentUsers = userService.findAllStudentUser()

        return ResponseEntity(studentUsers, HttpStatus.OK)
    }

    @ApiOperation(value = "Add a student to the current mentor")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/user/mentor/students/{id}/add")
    fun addStudentToMentor(@PathVariable("id") id: Long): ResponseEntity<*> {
        userService.addStudentToMentor(id)
        return ResponseEntity<HttpStatus>(HttpStatus.OK)
    }

    @ApiOperation(value = "Remove a student from the current mentor")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("user/mentor/students/{id}/remove")
    fun removeStudentFromMentor(@PathVariable("id") id: Long): ResponseEntity<*> {
        userService.removeStudentFromMentor(id)
        return ResponseEntity<HttpStatus>(HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/user/delete")
    fun deleteUser(): ResponseEntity<*> {
        userService.delete()

        return ResponseEntity<HttpStatus>(HttpStatus.OK)
    }
}