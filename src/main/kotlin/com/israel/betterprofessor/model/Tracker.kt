package com.israel.betterprofessor.model

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModelProperty
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

    @ApiModelProperty(required = true)
    var type: String? = null

    @ApiModelProperty(required = true)
    var name: String? = null

    // unix time UTC
    @ApiModelProperty(required = true)
    var deadline: Long? = null

    @ApiModelProperty(required = true)
    var shouldSendMessage: Boolean? = null

    // TODO constraint this
    var messageSenderUserId: Long? = null

    // not constraint, should be able to exist without the receiver
    var messageReceiverUserId: Long? = null

    var messageTitle: String? = null
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
            messageTitle: String?,
            messageText: String?,
            mentor: Mentor?
    ) {
        this.type = type
        this.name = name
        this.deadline = deadline
        this.shouldSendMessage = shouldSendMessage
        this.messageSenderUserId = messageSenderUserId
        this.messageReceiverUserId = messageReceiverUserId
        this.messageTitle = messageTitle
        this.messageText = messageText
        this.mentor = mentor
    }


}