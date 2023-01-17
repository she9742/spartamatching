package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

	public Admin(Long id, String username, String password, String nickname, String image, int point) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.image = image;
		this.point = point;
	}
}
