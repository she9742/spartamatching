package com.example.service;

import com.example.dto.MessageRequestDto;
import com.example.dto.MessageResponseDto;
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

    public ResponseEntity<List<MessageResponseDto>> getMessages(long talkId) {
        List<Message> messages = new ArrayList<>();
        messages = messageRepository.findAllByTalk(talkId);
        List<MessageResponseDto> messageResponseDtos = new ArrayList<>();
        for(Message message : messages) {
            messageResponseDtos.add(new MessageResponseDto(message));
        }
        return (ResponseEntity<List<MessageResponseDto>>) messageResponseDtos;
    }

    public MessageResponseDto sendMessages(long talkId, Client writer, MessageRequestDto messageRequestDto) {
        Talk talk = talkRepository.findById(talkId).orElseThrow(
                () -> new NullPointerException("톡방이 존재하지 않습니다.")
        );
        if(talk.isActivation()) {
            Message message = new Message(talkId,writer, messageRequestDto.getContent());
            messageRepository.save(message);
            return new MessageResponseDto(message);
        } else {
            return new MessageResponseDto("종료된 톡방에는 메시지를 보낼수 없습니다.");
        }
    }
}
