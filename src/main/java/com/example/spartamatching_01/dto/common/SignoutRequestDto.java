package com.example.spartamatching_01.dto.common;

import lombok.Getter;

@Getter
public class SignoutRequestDto {
    private String accessToken;
    private String refreshToken;

    public SignoutRequestDto(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}