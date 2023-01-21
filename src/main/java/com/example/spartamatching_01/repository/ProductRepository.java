package com.example.spartamatching_01.repository;

import com.example.spartamatching_01.entity.Client;
import com.example.spartamatching_01.entity.Product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllBy(Pageable pageable);
	List<Product> findBySellerId(Long SellerId);
//	Optional<Client> findByProductId(Long ProductId);

	void deleteAllBySellerId(Long sellerId);

}
