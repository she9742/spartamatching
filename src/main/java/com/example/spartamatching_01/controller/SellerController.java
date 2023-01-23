package com.example.spartamatching_01.controller;


import com.example.spartamatching_01.dto.*;
import com.example.spartamatching_01.entity.TradeReq;
import com.example.spartamatching_01.security.ClientDetailsImpl;
import com.example.spartamatching_01.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;



    //판매 상품 등록
    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> enrollMyProdcut(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerService.enrollMyProduct(requestDto, clientDetails.getClient()));
    }
    //판매 상품 수정
    @PatchMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> updateMyProduct(@PathVariable Long id, @RequestBody ProductRequestDto productRequestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.updateMyProduct(id,productRequestDto,clientDetails.getClient()));
    }

    //판매 상품 삭제
    @PutMapping("/products/{id}")
    public ResponseEntity<String> deleteMyProduct(@PathVariable Long id, @AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.deleteMyProduct(id,clientDetails.getClient()));
    }

    // 고객의 매칭요청 목록을 조회 이거로 페이징하는게 나을듯
    @GetMapping("/clientLists")
    public ResponseEntity<Page<ClientReqResponseDto>> getMatching(@RequestBody PageDto pageDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.getMatching(pageDto,clientDetails.getClient()));
    }

    // 고객의 거래요청 목록을 조회
    @GetMapping("/tradeLists")
    public ResponseEntity<List<TradeReq>> getTradeReq(@AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.getTradeReq(clientDetails.getClient()));
    }

    // 고객의 요청을 처리
    @PostMapping("/clients/{clientReqId}")
    public ResponseEntity<String> approveMatching(@PathVariable Long clientReqId ,@AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.approveMatching(clientReqId,clientDetails.getClient()));
    }

    // 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<SellerProfileResponseDto> getProfile(
            @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.getProfile(clientDetails.getClient()));
    }
    // 판매 상품 조회 페이징 필요
    @GetMapping("/products")
    public ResponseEntity<Page<AllProductResponseDto>> getMyProduct(@RequestBody PageDto pageDto,@AuthenticationPrincipal ClientDetailsImpl clientDetails ){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.getMyProduct(pageDto,clientDetails.getClient()));
    }

    @PostMapping("/sell/{tradeReqId}")
    public ResponseEntity<String> sellProduct(@PathVariable Long tradeReqId,@AuthenticationPrincipal ClientDetailsImpl clientDetails){
    return ResponseEntity.status(HttpStatus.OK).body(sellerService.sellProduct(tradeReqId,clientDetails.getClient()));
    }

    //셀러 프로필 업데이트
    @PutMapping("/profile")
    public ResponseEntity<String> updateSellerProfile(@RequestBody SellerProfileUpdateRequestDto requestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.updateProfile(requestDto,clientDetails.getClient()));
    }


}