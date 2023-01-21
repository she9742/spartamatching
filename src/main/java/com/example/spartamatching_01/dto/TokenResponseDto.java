package com.example.dto;

import lombok.Getter;

@Getter
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

    public TokenResponseDto(String accessToken,  String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
