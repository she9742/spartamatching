package com.example.spartamatching_01.dto.client;

import lombok.Getter;

@Getter
public class SigninResponseDto {
    private String accessToken;
    private String refreshToken;


    public SigninResponseDto(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
