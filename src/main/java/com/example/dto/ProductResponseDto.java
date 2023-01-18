package com.example.dto;

import com.example.entity.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {

	private String productName;
	private int point;

	public ProductResponseDto(Product product) {
		this.productName = product.getProductName();
		this.point = product.getPoint();
	}
}

