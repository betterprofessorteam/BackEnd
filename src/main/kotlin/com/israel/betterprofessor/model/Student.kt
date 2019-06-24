package com.israel.betterprofessor.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "students")
class Student {

    @JsonIgnore
    @Id
    @GeneratedValue
    var studentId: Long? = null

    var firstName: String? = null
    var lastName: String? = null

    @OneToOne
    @JsonIgnore
    var user: User? = null

    @ManyToMany(mappedBy = "students")
    @JsonIgnoreProperties("students")
    @JsonIgnore
    var mentors: MutableList<Mentor> = mutableListOf()

    constructor()
    constructor(firstName: String?, lastName: String?) {
        this.firstName = firstName
        this.lastName = lastName
    }


}