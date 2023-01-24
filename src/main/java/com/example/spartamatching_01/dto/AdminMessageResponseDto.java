package com.example.spartamatching_01.dto;

import com.example.spartamatching_01.entity.Message;
import lombok.Getter;

@Getter
public class AdminMessageResponseDto {
    private String accessToken;
    private String refreshToken;



    public AdminMessageResponseDto(String accessToken,String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
