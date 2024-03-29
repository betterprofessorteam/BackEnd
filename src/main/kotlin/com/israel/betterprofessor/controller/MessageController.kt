package com.israel.betterprofessor.controller

import com.israel.betterprofessor.model.Message
import com.israel.betterprofessor.model.User
import com.israel.betterprofessor.service.MessageService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class MessageController(private val messageService: MessageService) {

    @ApiOperation(value = "Send an instant message", response = Message::class)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/messages")
    fun sendMessage(@RequestBody message: Message): ResponseEntity<*> {
        val sentMessage = messageService.sendMessage(message)
        return ResponseEntity(sentMessage, HttpStatus.CREATED)
    }

    @ApiOperation(value = "Get all the messages of the current user", response = Message::class, responseContainer = "List")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/messages")
    fun getMessages(): ResponseEntity<*> {
        val messages = messageService.findAllMessage()
        return ResponseEntity(messages, HttpStatus.OK)
    }

    @ApiOperation(value = "Get all messages sent by the current user", response = Message::class, responseContainer = "List")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/messages/sent")
    fun getMessagesSent(): ResponseEntity<*> {
        val messages = messageService.findAllMessageSent()
        return ResponseEntity(messages, HttpStatus.OK)
    }

    @ApiOperation(value = "Get all messages received by the current user", response = Message::class, responseContainer = "List")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/messages/received")
    fun getMessagesReceived(): ResponseEntity<*> {
        val messages = messageService.findAllMessageReceived()
        return ResponseEntity(messages, HttpStatus.OK)
    }

    @ApiOperation(value = "Get all messages received by the current user that is unread", response = Message::class, responseContainer = "List")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/messages/received/unread")
    fun getMessagesReceivedUnread(): ResponseEntity<*> {
        val messages = messageService.findAllMessageReceivedUnread()
        return ResponseEntity(messages, HttpStatus.OK)
    }

    @ApiOperation(value = "Get all the messages with the user of the current user", response = Message::class, responseContainer = "List")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/messages/user/{id}")
    fun getMessagesWithUser(@PathVariable("id") id: Long): ResponseEntity<*> {
        val messages = messageService.findAllMessageWithUser(id)
        return ResponseEntity(messages, HttpStatus.OK)
    }

    @ApiOperation(value = "Set the message as read")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/messages/{id}/read")
    fun setMessageRead(@ApiParam(value = "messageId") @PathVariable("id") id: Long): ResponseEntity<*> {
        messageService.setMessageRead(id)
        return ResponseEntity<HttpStatus>(HttpStatus.OK)
    }

}