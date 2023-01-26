package com.example.spartamatching_01.dto.client;

import com.example.spartamatching_01.entity.Client;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
public class ProfileUpdateRequestDto {
    private String nickname;
    private String image;


}