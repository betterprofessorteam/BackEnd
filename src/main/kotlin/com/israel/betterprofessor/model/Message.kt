package com.israel.betterprofessor.model

import io.swagger.annotations.ApiModelProperty
import javax.persistence.*

@Entity
@Table(name = "messages")
class Message {

    @Id
    @GeneratedValue
    var messageId: Long? = null

    // unidirectional constraint
//    @ManyToOne(targetEntity = User::class)
//    @JoinColumn(name = "senderUserId")

    @Column(nullable = false)
    var senderUserId: Long? = null

    @ApiModelProperty(required = true)
//    @ManyToOne(targetEntity = User::class)
//    @JoinColumn(name = "receiverUserId")
    @Column(nullable = false)
    var receiverUserId: Long? = null

    // unix time UTC
    var timeSent: Long? = null

    var timeRead: Long? = null

    @ApiModelProperty(required = true)
    var text: String? = null

    constructor()
    constructor(senderUserId: Long?, receiverUserId: Long?, timeSent: Long?, text: String?) {
        this.senderUserId = senderUserId
        this.receiverUserId = receiverUserId
        this.timeSent = timeSent
        this.timeRead = 0
        this.text = text
    }


}