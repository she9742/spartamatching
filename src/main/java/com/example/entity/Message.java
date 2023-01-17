package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn
    @ManyToOne
    private Talk talk;

    @Column(nullable = false)
    private String witer;

    @Column(nullable = false)
    private String content;

    public Message(Talk talk, Client writer, String content) {
        this.talk = talk;
        this.witer = writer.getNickname();
        this.content = content;
    }
}
