package com.example.dto;

import com.example.entity.Client;
import lombok.Getter;

@Getter
public class ProfileUpdateRequestDto {
    private String nickname;
    private String image;

    public ProfileUpdateRequestDto(Client client) {
        this.nickname = client.getNickname();
        this.image = client.getImage();
    }