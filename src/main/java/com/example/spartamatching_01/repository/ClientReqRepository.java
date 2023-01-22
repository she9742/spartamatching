package com.example.spartamatching_01.repository;

import com.example.spartamatching_01.entity.ClientReq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface ClientReqRepository extends JpaRepository<ClientReq, Long> {



    Page<ClientReq> findAllBySellerId(Pageable pageable, Long id);
}
