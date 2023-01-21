package com.example.controller;

import com.example.dto.*;
import com.example.entity.Client;
import com.example.entity.Talk;
import com.example.security.ClientDetailsImpl;
import com.example.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RequiredArgsConstructor
@RestController
@RequestMapping("/client")
//@SecurityRequirement(name = "Bearer Authentication")
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.signup(signupRequestDto));
    }
    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody SigninRequestDto signinRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.signin(signinRequestDto));
    }

    @PostMapping("/seller")
    public ResponseEntity<String> applySeller(@RequestBody ApplySellerRequestDto applySellerRequestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.applySeller(clientDetails.getClient(),applySellerRequestDto));
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileUpdateResponseDto> updateProfile(@RequestBody ProfileUpdateRequestDto requestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.updateProfile(requestDto,clientDetails.getClient()));
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileUpdateResponseDto> getProfile(@AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getProfile(clientDetails.getClient()));
    }

    //전체 판매 상품 조회
    @GetMapping("/prodcuts")
    public ResponseEntity<Page<AllProductResponseDto>> getAllProduct(@RequestBody PageDto pageDto) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllProducts(pageDto));
    }

    //전체 판매자 조회
    @GetMapping("/sellers")
    public ResponseEntity<Page<AllSellerResponseDto>> getAllSellers(@RequestBody PageDto pageDto) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllSellers(pageDto));
    }

    //판매자 선택 조회
    @GetMapping("/seller/{id}")
    public ResponseEntity<SellerResponseDto> getSellerInfo(@PathVariable Long sellerId) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getSellerInfo(sellerId));
    }

    //전체메세지조회
    @GetMapping("/talk/{talkId}")
    public ResponseEntity<List<MessageResponseDto>> getMessages(@PathVariable Long talkId, @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getMessages(talkId,clientDetails.getClient()));
    }

    @PostMapping("/talk/{talkId}")
    public ResponseEntity<MessageResponseDto> sendMessage(@PathVariable Long talkId, @RequestBody MessageRequestDto messageRequestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.sendMessages(talkId,clientDetails.getClient(),messageRequestDto));
    }


    @PostMapping("/sellers/{sellerId}")
    public ResponseEntity<String> sendMatching(@PathVariable Long sellerId,Long clientId){
        return ResponseEntity.status(HttpStatus.OK).body(clientService.sendMatching(clientId,sellerId));
    }


    @PostMapping("/buy/{productid}")
    public ResponseEntity<String> buyProduct(Client client, @PathVariable Long productId){
        return ResponseEntity.status(HttpStatus.OK).body(clientService.buyProduct(client,productId));
    }
}