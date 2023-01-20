package com.example.controller;

import com.example.dto.*;
import com.example.entity.Client;
import com.example.entity.Talk;
import com.example.security.ClientDetailsImpl;
import com.example.service.ClientService;
import lombok.RequiredArgsConstructor;
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
        clientService.signup(signupRequestDto);
        return new ResponseEntity<>("회원 가입이 완료되었습니다.", HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody SigninRequestDto signinRequestDto) {
        clientService.signin(signinRequestDto);
        return new ResponseEntity<>("로그인이 되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/seller")
    public ResponseEntity<String> applySeller(@RequestBody ApplySellerRequestDto applySellerRequestDto,
                                              @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return clientService.applySeller(clientDetails.getClient(), applySellerRequestDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileUpdateResponseDto> updateProfile(@RequestBody ProfileUpdateRequestDto requestDto,
                                                                  @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return clientService.updateProfile(requestDto, clientDetails.getClient());
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileUpdateResponseDto> getProfile(
            @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return clientService.getProfile(clientDetails.getClient());
    }

    //전체 판매 상품 조회
    @GetMapping("/prodcuts")
    public ResponseEntity<List<AllProductResponseDto>> getAllProduct() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllProducts());
    }

    //전체 판매자 조회
    @GetMapping("/sellers")
    public ResponseEntity<List<AllSellerResponseDto>> getAllSellers() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllSellers());
    }

    //판매자 선택 조회
    @GetMapping("/seller/{id}")
    public ResponseEntity<SellerResponseDto> getSellerInfo(@PathVariable Long sellerId) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getSellerInfo(sellerId));
    }

    //전체메세지조회
    @GetMapping("/talk/{talkId}")
    public ResponseEntity<List<MessageResponseDto>> getMessages(@PathVariable Long talkId, @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return clientService.getMessages(talkId,clientDetails.getClient());
    }

    @PostMapping("/talk/{talkId}")
    public MessageResponseDto sendMessage(@PathVariable Long talkId, @RequestBody MessageRequestDto
            messageRequestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return clientService.sendMessages(talkId, clientDetails.getClient(), messageRequestDto);
    }


    @PostMapping("/sellers/{sellerId}")
    public String sendMatching(@PathVariable Long sellerId,Long clientId){
        return clientService.sendMatching(clientId,sellerId);
    }


    @PostMapping("/buy/{productid}")
    public String buyProduct(@PathVariable Long productid, Client client){
        return clientService.buyProduct(client,productid);
    }


}
