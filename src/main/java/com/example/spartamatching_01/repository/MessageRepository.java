package com.example.spartamatching_01.repository;

import com.example.spartamatching_01.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByTalk(Long talkId);
}
