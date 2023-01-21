package com.example.dto;

import com.example.entity.Client;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AllSellerResponseDto {

	private String nickname;
	private String image;
	private String category;
	private String about;

	public AllSellerResponseDto(Client client) {
		this.nickname = client.getNickname();
		this.image = client.getImage();
		this.category = client.getCategory();
		this.about = client.getAbout();
	}
}
