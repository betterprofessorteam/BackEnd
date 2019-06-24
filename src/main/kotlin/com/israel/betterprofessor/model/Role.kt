package com.israel.betterprofessor.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.israel.betterprofessor.model.Auditable

import javax.persistence.*

@Entity
@Table(name = "roles")
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var roleId: Long = 0

    @Column(nullable = false, unique = true)
    var name: String? = null

    @OneToMany(mappedBy = "role", cascade = [CascadeType.ALL])
    @JsonIgnoreProperties("role")
    var userRoles: MutableList<UserRole> = mutableListOf()

    constructor()

    constructor(name: String) {
        this.name = name
    }
}
