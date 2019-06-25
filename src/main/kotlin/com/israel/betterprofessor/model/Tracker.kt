package com.israel.betterprofessor.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "trackers")
class Tracker {

    companion object {
        const val TYPE_PROJECT = "Project"
        const val TYPE_FEEDBACK = "Feedback"
        const val TYPE_LETTER_OF_RECOMMENDATION = "Letter of Recommendation"

        fun isValidType(type: String): Boolean {
            return type == TYPE_PROJECT || type == TYPE_FEEDBACK || type == TYPE_LETTER_OF_RECOMMENDATION
        }
    }

    @Id
    @GeneratedValue
    var trackerId: Long? = null

    var type: String? = null
    var name: String? = null

    // unix time UTC
    var deadline: Long? = null

    var shouldSendMessage: Boolean? = null

    var messageSenderUserId: Long? = null

    // not constraint, should be able to exist without the receiver
    var messageReceiverUserId: Long? = null

    var messageText: String? = null

    @ManyToOne
    @JoinColumn(name = "mentorId")
    @JsonIgnore
    var mentor: Mentor? = null

    constructor()
    constructor(
            type: String?,
            name: String?,
            deadline: Long?,
            shouldSendMessage: Boolean?,
            messageSenderUserId: Long?,
            messageReceiverUserId: Long?,
            messageText: String?,
            mentor: Mentor?
    ) {
        this.type = type
        this.name = name
        this.deadline = deadline
        this.shouldSendMessage = shouldSendMessage
        this.messageSenderUserId = messageSenderUserId
        this.messageReceiverUserId = messageReceiverUserId
        this.messageText = messageText
        this.mentor = mentor
    }


}