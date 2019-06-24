package com.israel.betterprofessor.model

import javax.persistence.*

@Entity
@Table(name = "messages")
class Message {

    @Id
    @GeneratedValue
    var messageId: Long? = null

    // unidirectional constraint
    @ManyToOne(targetEntity = User::class)
    @JoinColumn(name = "senderUserId", referencedColumnName = "userId")
    var senderUserId: Long? = null

    @ManyToOne(targetEntity = User::class)
    @JoinColumn(name = "receiverUserId", referencedColumnName = "userId")
    var receiverUserId: Long? = null

    // unix time UTC
    var timeSent: Long? = null

    var timeRead: Long? = null
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