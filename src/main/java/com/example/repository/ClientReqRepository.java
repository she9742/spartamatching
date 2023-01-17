package com.example.repository;

import com.example.entity.ClientReq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientReqRepository extends JpaRepository<ClientReq, Long> {
    Optional<ClientReq> findBySellerId(Long sellerId);
}
