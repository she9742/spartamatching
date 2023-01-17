package com.example.repository;

import com.example.entity.Client;
import com.example.entity.SellerReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SellerReqRepository extends JpaRepository<SellerReq, Long> {

}
