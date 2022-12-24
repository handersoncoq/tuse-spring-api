package com.tuse.tuse.responses;

import com.tuse.tuse.models.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MessageResponse {

    private Long messageId;
    private Date date;
    private String title;
    private String content;
    private boolean isRead;

    public MessageResponse(Message message) {
        this.messageId = message.getMessageId();
        this.date = message.getSendDate();
        this.title = message.getTitle();
        this.content = message.getContent();
        this.isRead = message.isRead();
    }
}
