package com.example.dto;

import com.example.entity.ClientReq;
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
