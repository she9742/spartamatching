package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.example.dto.ProductRequestDto;

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

    @Column(nullable = false)
    private int point;

    @Column
    private Boolean activation;

    public Product(String productName, String information, Long sellerId, int point, Boolean activation) {
        this.productName = productName;
        this.information = information;
        this.sellerId = sellerId;
        this.point = point;
        this.activation = activation;
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
