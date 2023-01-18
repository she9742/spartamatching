package com.example.repository;

import com.example.entity.Client;
import com.example.entity.ClientReq;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ClientReqRepository extends JpaRepository<ClientReq, Long> {
    List<ClientReq> findAllBySellerId(Long sellerId);

}
