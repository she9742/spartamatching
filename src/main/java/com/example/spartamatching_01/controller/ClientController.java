package com.example.spartamatching_01.controller;


import com.example.spartamatching_01.dto.client.*;
import com.example.spartamatching_01.dto.common.AllProductResponseDto;
import com.example.spartamatching_01.dto.common.PageDto;
import com.example.spartamatching_01.dto.common.ReissueResponseDto;
import com.example.spartamatching_01.dto.common.SignoutRequestDto;
import com.example.spartamatching_01.dto.security.TokenRequestDto;
import com.example.spartamatching_01.entity.Client;

import com.example.spartamatching_01.jwt.JwtUtil;
import com.example.spartamatching_01.security.ClientDetailsImpl;
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
    private final JwtUtil jwtUtil;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.signup(signupRequestDto));
    }

    //로그인
    @PostMapping("/signin")
    public ResponseEntity<SigninResponseDto> signin(@RequestBody SigninRequestDto signinRequestDto, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.signin(signinRequestDto,response));
    }

    //판매자 요청
    @PostMapping("/sellers")
    public ResponseEntity<String> applySeller(@RequestBody ApplySellerRequestDto applySellerRequestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.applySeller(clientDetails.getClient(),applySellerRequestDto));
    }

    //사용자 정보 업데이트
    @PatchMapping("/profiles")
    public ResponseEntity<ProfileUpdateResponseDto> updateProfile(@RequestBody ProfileUpdateRequestDto requestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.updateProfile(requestDto,clientDetails.getClient()));
    }

    //사용자 정보 조회
    @GetMapping("/profiles")
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
    @GetMapping("/sellers/{sellerId}")
    public ResponseEntity<SellerResponseDto> getSellerInfo(@PathVariable Long sellerId) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getSellerInfo(sellerId));
    }

    //전체메세지조회
    @GetMapping("/talks/{talkId}")
    public ResponseEntity<List<MessageResponseDto>> getMessages(@PathVariable Long talkId, @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getMessages(talkId,clientDetails.getClient()));
    }

    //메시지 전송
    @PostMapping("/talks/{talkId}")
    public ResponseEntity<List<MessageResponseDto>> sendMessages(@PathVariable Long talkId, @RequestBody MessageRequestDto messageRequestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.sendMessages(talkId,clientDetails.getClient(),messageRequestDto));
    }

    //판매자에게 매칭 요청하기
    @PostMapping("/matching/{productId}")
    public ResponseEntity<String> sendMatching(@PathVariable Long productId, @AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return ResponseEntity.status(HttpStatus.OK).body(clientService.sendMatching(clientDetails.getClient().getId(), productId));
    }


    //물건 구매 요청 보내기
    @PostMapping("/buy/{productId}")
    public ResponseEntity<String> buyProduct(@PathVariable Long productId, @AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return ResponseEntity.status(HttpStatus.OK).body(clientService.buyProduct(clientDetails.getClient(),productId));
    }

    //토큰 재발급
    @PostMapping("/refresh")
    public ReissueResponseDto clientRefresh(HttpServletRequest request , @RequestBody TokenRequestDto tokenRequestDto){
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
            return clientService.reissue(refreshToken);
        }
        throw new IllegalStateException("리프레시 토큰과 엑세스토큰의 사용자가 일치하지 않습니다.");
    }

    //로그아웃
    @PostMapping("/signout")
    public ResponseEntity<String> signout(@RequestHeader("Authorization") String accessToken,
                       @RequestHeader("RefreshToken") String refreshToken) {
        String username = jwtUtil.getUserInfoFromToken(resolveToken(accessToken)).getSubject();
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.signout(new SignoutRequestDto(accessToken,refreshToken), username));
    }

    //로그아웃 메소드 보조
    private String resolveToken(String token) {
        return token.substring(7);
    }


}
