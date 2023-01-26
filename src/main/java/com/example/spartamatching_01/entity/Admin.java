package com.example.spartamatching_01.entity;

import javax.persistence.*;

import com.example.spartamatching_01.dto.admin.AdminSignupRequestDto;
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
	@Enumerated(value = EnumType.STRING)
	private UserRoleEnum role;


	public Admin(AdminSignupRequestDto adminSignupRequestDto, String password) {
		this.username = adminSignupRequestDto.getUsername();
		this.password = password;
		this.nickname = adminSignupRequestDto.getNickname();
		this.image = adminSignupRequestDto.getImage();
		this.role = UserRoleEnum.ADMIN;
	}
}
