package com.israel.betterprofessor.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.israel.betterprofessor.model.Auditable

import javax.persistence.*
import java.io.Serializable
import java.util.Objects

@Entity
@Table(name = "userRoles")
class UserRole : Auditable, Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnoreProperties("userRoles")
    var user: User? = null

    @Id
    @ManyToOne
    @JoinColumn(name = "roleId")
    @JsonIgnoreProperties("userRoles")
    var role: Role? = null

    constructor()

    constructor(user: User, role: Role) {
        this.user = user
        this.role = role
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is UserRole) {
            return false
        }
        val userRoles = other as UserRole?
        return user == userRoles!!.user && role == userRoles.role
    }

    override fun hashCode(): Int {
        return Objects.hash(user, role)
    }
}
