package com.example.spartamatching_01.repository;

import com.example.spartamatching_01.entity.ClientReq;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClientReqRepository extends JpaRepository<ClientReq, Long> {
    List<ClientReq> findAllBySellerId(Long sellerId);

}
