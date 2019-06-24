package com.israel.betterprofessor.model

import javax.persistence.*

@Entity
@Table(name = "projects")
class Project {

    @Id
    @GeneratedValue
    var id: Long? = null

    var title: String? = null
    var deadline: Long? = null

    @ManyToOne
    var student: Student? = null

}