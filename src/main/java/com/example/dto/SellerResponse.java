package com.example.dto;

import com.example.entity.Client;
import com.example.entity.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerResponse {

	private String nickname;
	private String image;
	private String category;
	private String about;

	public SellerResponse(Client client) {
		this.nickname = client.getNickname();
		this.image = client.getImage();
		this.category = client.getCategory();
		this.about = client.getAbout();
	}
}
