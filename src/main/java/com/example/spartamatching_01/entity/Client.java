package com.example.spartamatching_01.entity;


import com.example.spartamatching_01.dto.seller.SellerProfileUpdateRequestDto;
import com.example.spartamatching_01.dto.client.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
public class Client {

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

    @Column
    private int point;

    @Column
    private String category;

    @Column
    private String about;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    @JoinColumn
    @OneToMany
    private List<Product> products = new ArrayList<>();

    @Column(nullable = false)
    private boolean isSeller;

    public Client(SignupRequestDto signupRequestDto, String password) {
        this.username = signupRequestDto.getUsername();
        this.password = password;
        this.nickname = signupRequestDto.getNickname();
        this.image = signupRequestDto.getImage();
        this.isSeller = false;
        this.role = UserRoleEnum.USER;
    }

    public void updateClientProfile(String nickname, String image){
        this.nickname = nickname;
        this.image = image;
    }

    public void updateSellerProfile(SellerProfileUpdateRequestDto dto){
        this.nickname = dto.getNickname();
        this.image = dto.getImage();
        this.about =dto.getAbout();
        this.category = dto.getCategory();
    }

    public void updateSeller(String nickname, String image, Applicant applicant){
        this.nickname = nickname;
        this.image = image;
        this.about = applicant.getAbout();
        this.category = applicant.getCategory();
        this.isSeller = true;
    }

    public void rollbackClient(){
        this.about = null;
        this.category = null;
        this.isSeller = false;
    }
    public void deposit(int point){this.point = this.point + point;}

    public void withdraw(int point){
        this.point = this.point - point;
    }

    public boolean isSeller(){
        if(isSeller){
            return true;
        }
        return false;
    }
}
