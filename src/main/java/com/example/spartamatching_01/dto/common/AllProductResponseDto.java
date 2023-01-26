package com.example.spartamatching_01.dto.common;

import com.example.spartamatching_01.entity.Client;
import com.example.spartamatching_01.entity.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AllProductResponseDto {
	private String productName;
	private String category;
	private int price;
	private String sellerName;



	public AllProductResponseDto(Product product) {
		this.productName = product.getProductName();
		this.category = product.getCategory();
		this.price = product.getPoint();
		this.sellerName = product.getUsername();
	}
}
