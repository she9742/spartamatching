package com.example.spartamatching_01.repository;

import com.example.spartamatching_01.entity.SignoutAccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignoutAccessTokenRedisRepository extends CrudRepository<SignoutAccessToken, String> {
}
