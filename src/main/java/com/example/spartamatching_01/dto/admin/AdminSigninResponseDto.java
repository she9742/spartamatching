package com.example.spartamatching_01.dto.admin;

import lombok.Getter;

@Getter
public class AdminSigninResponseDto {
    private String accessToken;
    private String refreshToken;



    public AdminSigninResponseDto(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
