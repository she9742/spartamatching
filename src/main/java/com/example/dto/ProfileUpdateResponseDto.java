package com.example.dto;

import com.example.entity.Client;
import lombok.Getter;

@Getter
public class ProfileUpdateResponseDto {
    private String nickname;
    private String image;

    public ProfileUpdateResponseDto(Client client) {
        this.nickname = client.getNickname();
        this.image = client.getImage();
    }
}