package com.example.spartamatching_01.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long sellerId;



    public Matching(Long clientId, Long productId, Long sellerId) {
        this.clientId = clientId;
        this.productId = productId;
        this.sellerId = sellerId;
    }

}
