package com.example.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class ClientReq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long sellerId;



    public ClientReq(Client clientId, Client sellerId) {
        this.clientId = clientId;
        this.sellerId = sellerId;
    }

}
