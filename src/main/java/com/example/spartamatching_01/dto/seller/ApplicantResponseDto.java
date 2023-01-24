package com.example.spartamatching_01.dto.seller;

import com.example.spartamatching_01.entity.Matching;
import lombok.Getter;

@Getter
public class ApplicantResponseDto {

    private Long id;
    private Long clientId;

    public ApplicantResponseDto(Matching matching){
        this.id = matching.getId();
        this.clientId = matching.getClientId();
    }


}
