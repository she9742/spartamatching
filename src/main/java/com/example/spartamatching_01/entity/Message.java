package com.example.spartamatching_01.entity;

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
    @Column(nullable = false)
    private Long talk;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String content;

    public Message(Long talk, String writer, String content) {
        this.talk = talk;
        this.writer = writer;
        this.content = content;
    }
}
