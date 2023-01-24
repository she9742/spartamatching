package com.example.spartamatching_01.dto.common;


import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class PageDto {

    private int page; //몇 번째 페이지인지

    private int size; // 몇 개씩 들어가는지

    private String sortBy; // 어떤 것을 기준으로 할 것인지 ex) 상품명 or 판매자명

    private boolean isAsc; //오름차순 내림차순



    public PageDto(int page, int size, String sortBy, boolean isAsc) {

        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.isAsc = isAsc;

    }

}
