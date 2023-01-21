package com.example.spartamatching_01.repository;

import java.util.List;
import java.util.Optional;

import com.example.spartamatching_01.entity.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
	List<Client> findAllBy(Pageable pageable);
	Optional<Client> findByUsername(String username);
}
