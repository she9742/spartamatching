package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Talk;

public interface TalkRepository extends JpaRepository<Talk, Long> {
	Optional<Talk> findByClientId(Long ClientId);
	Optional<Talk> findBySellerId(Long SellerId);
}
