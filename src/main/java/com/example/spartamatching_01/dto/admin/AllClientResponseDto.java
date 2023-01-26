package com.example.spartamatching_01.dto.admin;

import com.example.spartamatching_01.entity.Client;

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