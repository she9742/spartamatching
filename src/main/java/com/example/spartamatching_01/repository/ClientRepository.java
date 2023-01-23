package com.example.spartamatching_01.repository;

import java.util.Optional;
import com.example.spartamatching_01.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
	Optional<Client> findByUsername(String username);
	Page<Client> findAllByIsSeller(Pageable pageable, boolean isSeller);
}
