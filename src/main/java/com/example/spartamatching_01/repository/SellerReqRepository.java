package com.example.spartamatching_01.repository;

import com.example.spartamatching_01.entity.SellerReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SellerReqRepository extends JpaRepository<SellerReq, Long> {

    Optional<SellerReq> findByClientId(Long clientId);


}
