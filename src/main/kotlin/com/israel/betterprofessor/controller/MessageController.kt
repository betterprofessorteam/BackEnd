package com.israel.betterprofessor.controller

import com.israel.betterprofessor.model.Message
import com.israel.betterprofessor.service.MessageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class MessageController(private val messageService: MessageService) {

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/messages")
    fun sendMessage(@RequestBody message: Message): ResponseEntity<*> {
        val sentMessage = messageService.sendMessage(message)
        return ResponseEntity(sentMessage, HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/messages")
    fun getMessages(): ResponseEntity<*> {
        val messages = messageService.findAllMessage()
        return ResponseEntity(messages, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/messages/user/{id}")
    fun getMessagesWithUser(@PathVariable("id") id: Long): ResponseEntity<*> {
        val messages = messageService.findAllMessageWithUser(id)
        return ResponseEntity(messages, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/messages/{id}/read")
    fun setMessageRead(@PathVariable("id") id: Long): ResponseEntity<*> {
        messageService.setMessageRead(id)
        return ResponseEntity<HttpStatus>(HttpStatus.OK)
    }

}