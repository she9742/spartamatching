package com.example.dto;

import com.example.entity.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private String writer;
    private String content;

    public MessageDto(Message message) {
        this.writer = message.getWriter();
        this.content = message.getContent();
    }
    public MessageDto(String content) {
        this.content = content;
        this.writer = "관리자";
    }
}
