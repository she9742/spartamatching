package com.example.spartamatching_01.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Long productId;

    @Column
    private boolean activation;

    @JoinColumn
    @OneToMany
    private List<Message> messages = new ArrayList<>();

    public Talk(Long clientId, Long productId) {
        this.clientId = clientId;
        this.productId = productId;
        this.activation = true;
    }

    public void closeTalk() {
        this.activation = false;
    }
}
