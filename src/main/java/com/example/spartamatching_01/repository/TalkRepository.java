package com.example.spartamatching_01.repository;

import java.util.Optional;

import com.example.spartamatching_01.entity.Talk;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface TalkRepository extends JpaRepository<Talk, Long> {


	Optional<Talk> findByClientIdAndProductId(Long clientId,Long productId);

}
