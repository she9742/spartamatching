package com.example.spartamatching_01.dto.common;

import lombok.Getter;

@Getter
public class ReissueResponseDto {
    private String accessToken;
    private String refreshToken;

    public ReissueResponseDto(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
