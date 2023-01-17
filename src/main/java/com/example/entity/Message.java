package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JoinColumn
    @ManyToOne
    private Talk talk;

    @Column(nullable = false)
    private String witer;

    @Column(nullable = false)
    private String content;

    public Message(Talk talk, Client writer, String content) {
        this.talk = talk.;
        this.witer = writer.getNickname();
        this.content = content;
    }
}
