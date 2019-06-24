package com.israel.betterprofessor.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "trackers")
class Tracker {

    @Id
    @GeneratedValue
    var trackerId: Long? = null

    var type: String? = null
    var name: String? = null
    var description: String? = null

    // unix time UTC
    var deadline: Long? = null

    var shouldSendMessage: Boolean? = null

    @JsonIgnore
    var messageFromUserId: Long? = null

    var messageToUserId: Long? = null
    var messageText: String? = null

    constructor()

}