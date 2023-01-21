package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Client;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
	Optional<Client> findByUsername(String username);
	Page<Client> findAll(Pageable pageable);

	List<Client> findAllByOrderByModifiedAtDesc(Pageable pageable);
}
