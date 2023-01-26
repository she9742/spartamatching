package com.example.spartamatching_01.repository;

import com.example.spartamatching_01.entity.Matching;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {

    Page<Matching> findAllBySellerId(Pageable pageable, Long id);
}
