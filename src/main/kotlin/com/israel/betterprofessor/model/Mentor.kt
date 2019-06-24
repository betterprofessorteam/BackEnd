package com.israel.betterprofessor.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "mentors")
class Mentor {

    @JsonIgnore
    @Id
    @GeneratedValue
    var mentorid: Long? = null

    var firstName: String? = null
    var lastName: String? = null

    @OneToOne
    @JsonIgnore
    var user: User? = null

    @ManyToMany
    @JoinTable(name = "mentorStudents",
            joinColumns = [JoinColumn(name = "mentorId")],
            inverseJoinColumns = [JoinColumn(name = "studentId")])
    @JsonIgnoreProperties("mentors")
    @JsonIgnore
    var students: MutableList<Student> = mutableListOf()

    constructor()
    constructor(firstName: String?, lastName: String?) {
        this.firstName = firstName
        this.lastName = lastName
    }


}