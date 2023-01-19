package com.example.controller;


import com.example.dto.MessageRequestDto;
import com.example.dto.MessageResponseDto;
import com.example.entity.Client;
import com.example.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/talk/{talkId}")
    public ResponseEntity<List<MessageResponseDto>> getMessages(@PathVariable Long talkId){
        return clientService.getMessages(talkId);
    }

    @PostMapping("/talk/{talkId}")
    public MessageResponseDto sendMessage(@PathVariable Long talkId, @RequestBody MessageRequestDto messageRequestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return clientService.sendMessages(talkId,clientDetails.getClient(),messageRequestDto);
    }


}
