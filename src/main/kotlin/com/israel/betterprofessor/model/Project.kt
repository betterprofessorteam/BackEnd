package com.israel.betterprofessor.model

import com.fasterxml.jackson.annotation.JsonIgnore
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
    @JoinColumn(name = "studentId")
    @JsonIgnore
    var student: Student? = null

    constructor()
    constructor(title: String?, deadline: Long?, student: Student?) {
        this.title = title
        this.deadline = deadline
        this.student = student
    }
}