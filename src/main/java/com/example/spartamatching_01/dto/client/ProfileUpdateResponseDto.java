package com.example.spartamatching_01.dto.client;

import com.example.spartamatching_01.entity.Client;
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