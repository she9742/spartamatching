package com.example.repository;

import com.example.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository< Client, Long> {
}
