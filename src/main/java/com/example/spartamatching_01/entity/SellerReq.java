package com.example.entity;

import com.example.dto.ApplySellerRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class SellerReq {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String category;
    private String about;

    @Column
    private Long clientId;

    public SellerReq(Long clientId, ApplySellerRequestDto applySellerRequestDto) {
        this.clientId = clientId;
        this.category = applySellerRequestDto.getCategory();
        this.about = applySellerRequestDto.getAbout();
    }
}
