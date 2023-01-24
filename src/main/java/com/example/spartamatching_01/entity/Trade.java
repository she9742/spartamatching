package com.example.spartamatching_01.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
public class Trade {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private Long productId;


    public Trade(Long clientId, Long sellerId,Long productId) {
        this.clientId = clientId;
        this.sellerId = sellerId;
        this.productId = productId;
    }
}