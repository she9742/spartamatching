package com.example.dto;

import com.example.entity.Client;
import lombok.Getter;




public class ProfileUpdateDto {

    @Getter
    public static class Res{
        private String nickname;
        private String image;

        public Res(Client client) {
            this.nickname = client.getNickname();
            this.image = client.getImage();
        }
    }
    @Getter
    public static class Req{

        private String nickname;
        private String image;
        public Req(Client client){
            this.nickname = client.getNickname();
            this.image = client.getImage();
        }


    }
}
