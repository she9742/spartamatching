package com.example.controller;

import com.example.dto.ProfileUpdateRequestDto;
import com.example.dto.ProfileUpdateResponseDto;
import com.example.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    @PutMapping("/profile")
    public ResponseEntity<ProfileUpdateResponseDto> updateProfile(@RequestBody ProfileUpdateRequestDto requestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return clientService.updateProfile(requestDto,clientDetails.getClient());
    }

    @GetMapping("/profile")

    public ResponseEntity<ProfileUpdateResponseDto> getProfile(@AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return clientService.getProfile(clientDetails.getClient());
    }
}
