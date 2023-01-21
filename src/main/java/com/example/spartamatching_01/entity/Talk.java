package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.entity.Message;
import com.example.entity.Client;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class  Talk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private boolean activation;

    @JoinColumn
    @OneToMany
    private List<Message> messages = new ArrayList<>();

    public Talk(Long client, Long seller) {
        this.clientId = client;
        this.sellerId = seller;
        this.activation = true;
    }

    public void closeTalk() {
        this.activation = false;
    }
}
