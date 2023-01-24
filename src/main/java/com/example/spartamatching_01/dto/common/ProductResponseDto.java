package com.example.spartamatching_01.dto.common;

import com.example.spartamatching_01.entity.Product;
import lombok.Getter;


@Getter
public class ProductResponseDto {
    private String productName;
    private String information;
    private int point;

    public ProductResponseDto(Product product){
        this.productName = product.getProductName();
        this.information = product.getInformation();
        this.point = product.getPoint();

    }

}
