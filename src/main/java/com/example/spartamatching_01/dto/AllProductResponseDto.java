package com.example.spartamatching_01.dto;

import com.example.spartamatching_01.entity.Client;
import com.example.spartamatching_01.entity.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllProductResponseDto {
	private String productName;
	private String category;
	private int price;
	private String sellerName;

	public AllProductResponseDto(Product product, Client client) {
		this.productName = product.getProductName();
		this.category = client.getCategory();
		this.price = product.getPoint();
		this.sellerName = client.getUsername();
	}
}
