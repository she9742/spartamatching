package com.example.spartamatching_01.dto.seller;

import com.example.spartamatching_01.entity.Client;
import lombok.Getter;

@Getter
public class SellerProfileResponseDto {

    private String nickname;
    private String image;
    private String category;
    private String about;

    public SellerProfileResponseDto(Client client) {
        this.nickname = client.getNickname();
        this.image = client.getImage();
        this.category = client.getCategory();
        this.about = client.getAbout();
    }
}
