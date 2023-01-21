package com.example.dto;

import com.example.entity.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MessageResponseDto {
    private String writer;
    private String content;

    public MessageResponseDto(Message message) {
        this.writer = message.getWriter();
        this.content = message.getContent();
    }
    public MessageResponseDto(String content) {
        this.content = content;
        this.writer = "관리자";
    }
}
