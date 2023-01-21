package com.example.dto;

import lombok.Getter;

@Getter
public class WithdrawPointRequestDto {

    private Long clientId;
    private int point;

    public WithdrawPointRequestDto(Long clientId, int point) {
        this.clientId = clientId;
        this.point = point;
    }
}
