package com.example.spartamatching_01.dto;

import com.example.spartamatching_01.entity.ClientReq;
import lombok.Getter;

@Getter
public class ClientReqResponseDto {

    private Long id;
    private Long clientId;

    public ClientReqResponseDto(ClientReq clientReq){
        this.id = clientReq.getId();
        this.clientId = clientReq.getClientId();
    }


}
