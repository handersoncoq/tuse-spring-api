package com.tuse.tuse.services;

import com.tuse.tuse.models.Message;
import com.tuse.tuse.models.User;
import com.tuse.tuse.repositories.MessageRepo;
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
    public Message save(Message message){
        return messageRepo.save(message);
    }

    public List<Message> getAllUserMessages(User user){
        return messageRepo.findMessagesByUserId(user.getUserId());
    }
}
