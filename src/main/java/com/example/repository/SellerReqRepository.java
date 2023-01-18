package com.example.repository;

import com.example.entity.SellerReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SellerReqRepository extends JpaRepository<SellerReq, Long> {

    Optional<SellerReq> findByClientId(Long ClientId);

}
