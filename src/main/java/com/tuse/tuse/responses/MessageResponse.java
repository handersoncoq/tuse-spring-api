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

    private Date date;
    private String title;
    private String content;

    public MessageResponse(Message message) {
        this.date = message.getSendDate();
        this.title = message.getTitle();
        this.content = message.getContent();
    }
}
