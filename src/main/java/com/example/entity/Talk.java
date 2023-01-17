package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Talk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private Client client;

    @Column(nullable = false)
    private Client seller;
    
    @Column(nullable = false)
    private boolean open;

    @JoinColumn
    @OneToMany
    private List<Message> messages = new ArrayList<>();

    public Talk(Client client, Client seller) {
        this.client = client;
        this.seller = seller;
        this.open = true;
    }

    public void closeTalk() {
        this.open = false;
    }
}
