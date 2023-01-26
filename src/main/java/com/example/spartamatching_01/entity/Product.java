package com.example.spartamatching_01.entity;


import com.example.spartamatching_01.dto.seller.ProductRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false,unique = true)
    private String productName;

    @Column
    private String information;

    @Column
    private Long sellerId;

    @Column
    private String username;

    @Column
    private String category;

    @Column(nullable = false)
    private int point;

    @Column
    private Boolean activation;


    public Product(ProductRequestDto dto,Client client) {
        this.productName = dto.getProductName();
        this.information = dto.getInformation();
        this.sellerId = client.getId();
        this.username = client.getUsername();
        this.category = client.getCategory();
        this.sellerId = dto.getSellerId();
        this.point = dto.getPoint();
        this.activation = true;
    }
    public void update(ProductRequestDto productRequestDto){
        this.productName = productRequestDto.getProductName();
        this.information = productRequestDto.getInformation();
        this.point = productRequestDto.getPoint();
    }

    public void unactivate(){
        this.activation=false;
    }


}
