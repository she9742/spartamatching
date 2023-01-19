package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.dto.AdminSignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String nickname;
	@Column(nullable = false)
	private String image;
	@Column(nullable = false)
	private int point;

	public Admin(AdminSignupRequestDto adminSignupRequestDto, String password) {
		this.username = adminSignupRequestDto.getUsername();
		this.password = password;
		this.nickname = adminSignupRequestDto.getNickname();
		this.image = adminSignupRequestDto.getImage();
	}

	public void withdraw(int point){
		this.point = this.point - point;
	}
}
