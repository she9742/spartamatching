package com.example.spartamatching_01.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDto {
    private String Content;

    public MessageRequestDto(String content) {
        Content = content;
    }
}
