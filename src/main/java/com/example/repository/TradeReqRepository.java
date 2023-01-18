package com.example.repository;


import com.example.entity.TradeReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeReqRepository extends JpaRepository<TradeReq,Long> {
    Optional<TradeReq> findBySellerIdAndClientId(Long sellerId,Long clientId);

    List<TradeReq> findAllBySellerId(Long sellerId);
}
