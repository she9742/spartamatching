package com.example.dto;

import com.example.entity.Client;
import lombok.Getter;

@Getter
public class ProfileUpdateResonseDto {
    private String nickname;
    private String image;

    public ProfileUpdateResonseDto(Client client) {
        this.nickname = client.getNickname();
        this.image = client.getImage();
    }
}
