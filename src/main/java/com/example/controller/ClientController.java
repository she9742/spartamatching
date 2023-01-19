package com.example.controller;
import java.util.List;

import com.example.dto.AllProductResponseDto;
import com.example.dto.AllSellerResponseDto;
import com.example.dto.ApplySellerRequestDto;
import com.example.dto.ProfileUpdateRequestDto;
import com.example.dto.ProfileUpdateResponseDto;
import com.example.dto.SellerResponseDto;
import com.example.dto.SigninRequestDto;
import com.example.dto.SignupRequestDto;
import com.example.service.ClientService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        clientService.signup(signupRequestDto);
        return new ResponseEntity<>("회원 가입이 완료되었습니다.", HttpStatus.CREATED);
    }

    @PostMapping("signin")
    public ResponseEntity<String> signin(@RequestBody SigninRequestDto signinRequestDto) {
        clientService.signin(signinRequestDto);
        return new ResponseEntity<>("로그인이 되었습니다.", HttpStatus.OK);
    }

    @PostMapping("seller")
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
        //전체 판매 상품 조회
        @GetMapping("/client/prodcuts")
        public ResponseEntity<List<AllProductResponseDto>> getAllProduct () {
            return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllProducts());
        }

        //전체 판매자 조회
        @GetMapping("/client/sellers")
        public ResponseEntity<List<AllSellerResponseDto>> getAllSellers () {
            return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllSellers());
        }

        //판매자 선택 조회
        @GetMapping("/client/seller/{id}")
        public ResponseEntity<SellerResponseDto> getSellerInfo (@PathVariable Long sellerId){
            return ResponseEntity.status(HttpStatus.OK).body(clientService.getSellerInfo(sellerId));
        }

    }
}
