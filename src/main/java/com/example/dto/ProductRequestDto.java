package com.example.dto;

import com.example.entity.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
	private String productName;
	private String information;
	private int point;

	public ProductRequestDto(Product product) {
		this.productName = product.getProductName();
		this.information = product.getInformation();
		this.point = product.getPoint();
	}
}
