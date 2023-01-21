package com.example.spartamatching_01.dto;

import com.example.spartamatching_01.entity.Client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerResponseDto {

	private String nickname;
	private String image;
	private String category;
	private String about;

	public SellerResponseDto(Client client) {
		this.nickname = client.getNickname();
		this.image = client.getImage();
		this.category = client.getCategory();
		this.about = client.getAbout();
	}
}
