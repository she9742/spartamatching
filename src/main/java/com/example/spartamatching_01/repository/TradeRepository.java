package com.example.spartamatching_01.repository;
import com.example.spartamatching_01.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade,Long> {


    List<Trade> findAllBySellerId(Long sellerId);


}
