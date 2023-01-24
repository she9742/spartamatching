package com.example.spartamatching_01.dto.security;

import lombok.Getter;

@Getter
public class SecurityExceptionDto {

    private int statusCode;
    private String msg;

    public SecurityExceptionDto(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

}
