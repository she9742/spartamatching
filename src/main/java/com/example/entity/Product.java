package com.example.entity;

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
}
