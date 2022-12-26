package com.tuse.tuse.services;

import com.tuse.tuse.models.Message;
import com.tuse.tuse.models.User;
import com.tuse.tuse.repositories.MessageRepo;
import com.tuse.tuse.responses.MessageResponse;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepo messageRepo;

    @Autowired
    public MessageService( MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Transactional
    public void save(Message message){
        messageRepo.save(message);
    }

    @Transactional
    public void update(Long msgId){

        Message foundMsg = messageRepo.findById(msgId).orElseThrow(ResourceNotFoundException::new);
        foundMsg.setRead(true);
        save(foundMsg);

    }

    public List<Message> getAllUserMessages(User user){
        return messageRepo.findMessagesByUserId(user.getUserId());
    }

    public Integer getNumberOfUnReadMessages(User user){
        List<Message> allMessages = messageRepo.findMessagesByUserId(user.getUserId());

        return (int) allMessages.stream()
                .filter((message -> !message.isRead())).count();
    }

    public MessageResponse getMessageById(Long msgId){
        Message msg = messageRepo.findById(msgId).orElseThrow(ResourceNotFoundException::new);
        return new MessageResponse(msg);
    }
}
