package com.tuse.tuse.controllers;

import com.tuse.tuse.models.User;
import com.tuse.tuse.responses.MessageResponse;
import com.tuse.tuse.services.MessageService;
import com.tuse.tuse.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://127.0.0.1:3000"},  allowCredentials = "true")

public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping
    public List<MessageResponse> getMessagesToUser(){
        User user = userService.getSessionUser();
        return messageService
                .getAllUserMessages(user)
                .stream()
                .map(MessageResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/count/unRead")
    public Integer getNumberOfUnReadMessages(){
        User user = userService.getSessionUser();
        return messageService.getNumberOfUnReadMessages(user);
    }

    @PutMapping("/isRead/{msgId}")
    public void update(@PathVariable Long msgId){
        messageService.update(msgId);
    }

    @GetMapping("/{msgId}")
    public MessageResponse getMessageById(@PathVariable Long msgId){

        return messageService.getMessageById(msgId);
    }
}
