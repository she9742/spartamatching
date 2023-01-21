package com.example.dto;

import com.example.entity.Client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllClientResponseDto {

	private String nickname;
	private String image;

	private int point;

	public AllClientResponseDto(Client client) {
		this.nickname = client.getNickname();
		this.image = client.getImage();
		this.point = client.getPoint();
	}
}