package com.example.service;

import com.example.dto.MessageDto;
import com.example.entity.Client;
import com.example.entity.Message;
import com.example.entity.Talk;
import com.example.repository.MessageRepository;
import com.example.repository.TalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final TalkRepository talkRepository;
    private final MessageRepository messageRepository;

    public ResponseEntity<List<MessageDto>> getMessages(long talkId) {
        List<Message> messages = new ArrayList<>();
        messages = messageRepository.findAllByTalk(talkId);
        List<MessageDto> messageDtos = new ArrayList<>();
        for(Message message : messages) {
            messageDtos.add(new MessageDto(message));
        }
        return (ResponseEntity<List<MessageDto>>)messageDtos;
    }

    public MessageDto sendMessages(long talkId, Client writer,String content) {
        Talk talk = talkRepository.findById(talkId).orElseThrow(
                () -> new NullPointerException("톡방이 존재하지 않습니다.")
        );
        if(talk.isActivation()) {
            Message message = new Message(talkId,writer,content);
            messageRepository.save(message);
            return new MessageDto(message);
        } else {
            return new MessageDto("종료된 톡방에는 메시지를 보낼수 없습니다.");
        }
    }
}
