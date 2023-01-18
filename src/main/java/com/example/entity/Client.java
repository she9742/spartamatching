package com.example.entity;


import com.example.dto.SellerProfileUpdateRequestDto;
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

    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String about;

    @JoinColumn
    @OneToMany
    private List<Product> products = new ArrayList<>();

    @Column(nullable = false)
    private boolean isSeller;

    public Client(String username, String password, String nickname, String image, int point) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
        this.point = point;
        this.isSeller = false;
    }

    public void updateClientProfile(String nickname, String image){
        this.nickname = nickname;
        this.image = image;
        //닉네임 이미지
    }

    public void updateSellerProfile(SellerProfileUpdateRequestDto dto){
        //닉네임 이미지 어바웃 카테코리
        this.nickname = dto.getNickname();
        this.image = dto.getImage();
        this.about =dto.getAbout();
        this.category = dto.getCategory();

    }

    public void updateSeller(String nickname, String image, String about, String category){
        //닉네임 이미지 어바웃 카테고리
        this.nickname = nickname;
        this.image = image;
        this.about =about;
        this.category = category;
        this.isSeller = true;
    }
    public void deposit(int point){this.point = this.point + point;}

    public void withdraw(int point){
        this.point = this.point - point;
    }

    public boolean getisSeller(){
        if(isSeller){
            return true;
        }
        return false;
    }
}
