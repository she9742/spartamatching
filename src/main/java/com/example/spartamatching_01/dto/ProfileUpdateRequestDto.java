package com.example.spartamatching_01.dto;

import com.example.spartamatching_01.entity.Client;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
public class ProfileUpdateRequestDto {
    private String nickname;
    private String image;

    public ProfileUpdateRequestDto(String nickname, String image) {
        this.nickname = nickname;
        this.image = image;
    }

    public ProfileUpdateRequestDto(Client client) {
        this.nickname = client.getNickname();
        this.image = client.getImage();
    }

    public ProfileUpdateRequestDto(String nickname, String image) {
        this.nickname = nickname;
        this.image = image;
    }
}