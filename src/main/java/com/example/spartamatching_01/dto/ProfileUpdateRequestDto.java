package com.example.spartamatching_01.dto;

import com.example.spartamatching_01.entity.Client;
import lombok.Getter;

@Getter
public class ProfileUpdateRequestDto {
    private String nickname;
    private String image;

    public ProfileUpdateRequestDto(Client client) {
        this.nickname = client.getNickname();
        this.image = client.getImage();
    }
}