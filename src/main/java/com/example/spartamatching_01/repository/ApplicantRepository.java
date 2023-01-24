package com.example.spartamatching_01.repository;

import com.example.spartamatching_01.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    Optional<Applicant> findByClientId(Long clientId);


}
