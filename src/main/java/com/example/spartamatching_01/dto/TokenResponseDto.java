package com.example.spartamatching_01.dto;

import lombok.Getter;

@Getter
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

    public TokenResponseDto(String accessToken) {
        //public TokenResponseDto(String accessToken,  String refreshToken) {
        this.accessToken = accessToken;
        //this.refreshToken = refreshToken;
    }

    public TokenResponseDto(String accessToken,String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
