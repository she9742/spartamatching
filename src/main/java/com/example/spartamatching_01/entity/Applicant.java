package com.example.spartamatching_01.entity;

import com.example.spartamatching_01.dto.client.ApplySellerRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String category;

    @Column
    private String about;

    @Column
    private Long clientId;

    public Applicant(Long clientId, ApplySellerRequestDto applySellerRequestDto) {
        this.clientId = clientId;
        this.category = applySellerRequestDto.getCategory();
        this.about = applySellerRequestDto.getAbout();
    }
}
