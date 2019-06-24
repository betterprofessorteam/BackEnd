package com.israel.betterprofessor.repository

import com.israel.betterprofessor.model.Message
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface MessageRepository : CrudRepository<Message, Long> {

    @Query(value = "SELECT * FROM messages WHERE (sender_user_id=:currentUserId AND receiver_user_id=:otherUserId) OR (sender_user_id=:otherUserId AND receiver_user_id=:currentUserId)", nativeQuery = true)
    fun findAllMessageWithUser(currentUserId: Long, otherUserId: Long): MutableList<Message>

    @Query(value = "SELECT * FROM messages WHERE sender_user_id=:currentUserId OR receiver_user_id=:currentUserId", nativeQuery = true)
    fun findAllMessageByUser(currentUserId: Long): MutableList<Message>

    @Query(value = "SELECT * FROM messages WHERE time_read=0 AND receiver_user_id=:currentUserId", nativeQuery = true)
    fun findAllMessageByUserUnread(currentUserId: Long)

}