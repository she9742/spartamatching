package com.example.spartamatching_01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
@EnableCaching
public class Spartamatching01Application {

    public static void main(String[] args) {
        SpringApplication.run(Spartamatching01Application.class, args);
    }

}
