package com.example.spartamatching_01.dto;

import com.example.spartamatching_01.entity.Message;
import lombok.Getter;

@Getter
public class AdminMessageResponseDto {
    private String writer;
    private String content;

    public AdminMessageResponseDto(Message message) {
        this.writer = message.getWriter();
        this.content = message.getContent();
    }
    public AdminMessageResponseDto(String content) {
        this.content = content;
        this.writer = "관리자";
    }
}
