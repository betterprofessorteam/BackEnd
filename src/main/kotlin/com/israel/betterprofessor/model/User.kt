package com.israel.betterprofessor.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.israel.betterprofessor.model.Auditable
import io.swagger.annotations.ApiModelProperty
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import javax.persistence.*
import java.util.ArrayList

// User is considered the parent entity

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var userId: Long? = null

    @ApiModelProperty(required = true)
    @Column(nullable = false, unique = true)
    var username: String? = null

    @ApiModelProperty(required = true)
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password: String? = null

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    @JsonIgnore
    @JsonIgnoreProperties("user")
    var userRoles: MutableList<UserRole> = mutableListOf()

    @ApiModelProperty(required = true)
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var mentorData: Mentor? = null

    @ApiModelProperty(required = true)
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var studentData: Student? = null

//    @OneToMany(cascade = [CascadeType.ALL])
//    @JoinColumn(name = "senderUserId", referencedColumnName = "userId")
//    @JsonIgnore
//    var messageSent: MutableList<Message> = mutableListOf()
//
//    @OneToMany(cascade = [CascadeType.ALL])
//    @JoinColumn(name = "receiverUserId", referencedColumnName = "userId")
//    @JsonIgnore
//    var messageReceived: MutableList<Message> = mutableListOf()

    constructor()

    constructor(username: String, password: String, userRoles: MutableList<UserRole>) {
        this.username = username
        setPasswordEncrypt(password)

        for (ur in userRoles) {
            ur.user = this
        }
        this.userRoles = userRoles
    }

    @JsonIgnore
    fun getAuthority(): List<SimpleGrantedAuthority> {
        val rtnList = ArrayList<SimpleGrantedAuthority>()

        for (r in this.userRoles) {
            val myRole = "ROLE_" + r.role!!.name!!.toUpperCase()
            rtnList.add(SimpleGrantedAuthority(myRole))
        }

        return rtnList
    }

    @JsonIgnore
    fun setPasswordEncrypt(password: String) {
        val passwordEncoder = BCryptPasswordEncoder()
        this.password = passwordEncoder.encode(password)
    }
}
