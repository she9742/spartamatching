package com.example.spartamatching_01.controller;


import com.example.spartamatching_01.dto.*;
import com.example.spartamatching_01.entity.Client;
import com.example.spartamatching_01.jwt.JwtUtil;
import com.example.spartamatching_01.security.ClientDetailsImpl;
import com.example.spartamatching_01.service.AdminService;
import com.example.spartamatching_01.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;
    private final AdminService adminService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.signup(signupRequestDto));
    }
    @PostMapping("/signin")
    public ResponseEntity<MessageResponseDto> signin(@RequestBody SigninRequestDto signinRequestDto, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.signin(signinRequestDto,response));
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
    @GetMapping("/products")
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


    @PostMapping("/refresh")
    public TokenResponseDto clientRefresh(HttpServletRequest request, @RequestBody TokenRequestDto tokenRequestDto){
        //bearer 제거
        String resolvedAccessToken = jwtUtil.resolveAccessToken(tokenRequestDto.getAccessToken());

        //Access 토큰 username가져오기
        //인증을 확인하고 authenticationAccessToken 변수에 토큰저장
        Authentication authenticationAccessToken = jwtUtil.getAuthentication(resolvedAccessToken);
        //DB에 접근하여 위에서만든 토큰에 해당하는 유저의 정보 반환
        Client accessUser = clientService.findByUsername(authenticationAccessToken.getName());

        //Refrest 토큰 username가져오기
        String refreshToken = request.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER);
        String resolvedRefreshToken = jwtUtil.resolveRefreshToken(refreshToken);
        //인증을 확인하고 authenticationFreshToken 변수에 토큰저장
        Authentication authenticationFreshToken = jwtUtil.getAuthentication(resolvedRefreshToken);
        //DB에 접근하여 위에서만든 토큰에 해당하는 유저의 정보 반환
        Client refreshUser = clientService.findByUsername(authenticationFreshToken.getName());

        //두개 비교 후 맞으면 재발행 ->엑세스토큰과 리프레시토큰의 유저정보가 같은지 확인하는작업
        if (accessUser == refreshUser) {
            return clientService.reissue(refreshUser.getUsername(), refreshUser.getRole());
        }
        throw new IllegalStateException("리프레시 토큰과 엑세스토큰의 사용자가 일치하지 않습니다.");
    }

}
