package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Client;

public interface ClientRepository extends JpaRepository {
	List<Client> findAllBy(Pageable pageable);
}
