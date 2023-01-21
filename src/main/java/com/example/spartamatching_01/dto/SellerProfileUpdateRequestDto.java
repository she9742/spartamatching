package com.example.dto;

import com.example.entity.Client;
import lombok.Getter;

@Getter
public class SellerProfileUpdateRequestDto {

    private String nickname;
    private String image;
    private String about;
    private String category;

    public SellerProfileUpdateRequestDto(Client client){
        this.nickname = client.getNickname();
        this.image = client.getImage();
        this.about = client.getAbout();
        this.category = client.getCategory();
    }

}
