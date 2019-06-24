package com.israel.betterprofessor.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.annotations.ApiModelProperty
import javax.persistence.*

@Entity
@Table(name = "mentors")
class Mentor {

    @JsonIgnore
    @Id
    @GeneratedValue
    var mentorid: Long? = null

    @ApiModelProperty(required = true)
    var firstName: String? = null

    @ApiModelProperty(required = true)
    var lastName: String? = null

    @ApiModelProperty(hidden = true)
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