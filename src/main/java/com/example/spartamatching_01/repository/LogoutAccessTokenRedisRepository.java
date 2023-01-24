package com.example.spartamatching_01.repository;

import com.example.spartamatching_01.entity.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
