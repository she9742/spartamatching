package com.example.repository;

import com.example.entity.Client;
import com.example.entity.Product;

import com.example.entity.SellerReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllBy(Pageable pageable);
	List<Product> findBySellerId(Long SellerId);

	void deleteAllBySellerId(Long sellerId);

}
