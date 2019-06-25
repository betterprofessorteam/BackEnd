package com.israel.betterprofessor.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.annotations.ApiModelProperty
import javax.persistence.*

@Entity
@Table(name = "students")
class Student {

    @JsonIgnore
    @Id
    @GeneratedValue
    var studentId: Long? = null

    @ApiModelProperty(required = true)
    var firstName: String? = null

    @ApiModelProperty(required = true)
    var lastName: String? = null

    @OneToOne
    @JsonIgnore
    var user: User? = null

    @ManyToMany(mappedBy = "students")
    @JsonIgnoreProperties("students")
    @JsonIgnore
    var mentors: MutableList<Mentor> = mutableListOf()

//    @OneToMany(cascade = [CascadeType.ALL])
//    var projects: MutableList<Project> = mutableListOf()

    constructor()
    constructor(firstName: String?, lastName: String?) {
        this.firstName = firstName
        this.lastName = lastName
    }


}