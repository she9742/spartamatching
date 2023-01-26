package com.example.spartamatching_01.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;

@Getter
@RedisHash("signoutAccessToken")
@AllArgsConstructor
@Builder
public class SignoutAccessToken {

    @Id
    private String id;

    private String username;

    @TimeToLive
    private Long expiration;

    public static SignoutAccessToken of(String accessToken, String username, Long remainingMilliSeconds) {
        return SignoutAccessToken.builder()
                .id(accessToken)
                .username(username)
                .expiration(remainingMilliSeconds / 1000)
                .build();
    }
}