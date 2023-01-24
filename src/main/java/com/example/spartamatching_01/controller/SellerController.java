package com.example.spartamatching_01.controller;


import com.example.spartamatching_01.dto.common.AllProductResponseDto;
import com.example.spartamatching_01.dto.common.PageDto;
import com.example.spartamatching_01.dto.common.ProductResponseDto;
import com.example.spartamatching_01.dto.seller.ApplicantResponseDto;
import com.example.spartamatching_01.dto.seller.ProductRequestDto;
import com.example.spartamatching_01.dto.seller.SellerProfileResponseDto;
import com.example.spartamatching_01.dto.seller.SellerProfileUpdateRequestDto;
import com.example.spartamatching_01.entity.Trade;
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
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;



    //판매 상품 등록
    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> enrollMyProduct(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails){
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

    // 고객의 매칭요청 목록을 조회
    @GetMapping("/client-list")
    public ResponseEntity<Page<ApplicantResponseDto>> getMatching(@RequestBody PageDto pageDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.getMatching(pageDto,clientDetails.getClient()));
    }

    // 고객의 거래요청 목록을 조회
    @GetMapping("/trade-list")
    public ResponseEntity<List<Trade>> getTradeList(@AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.getTradeList(clientDetails.getClient()));
    }

    // 고객의 요청을 처리
    @PostMapping("/approve/{clientId}")
    public ResponseEntity<String> approveMatching(@PathVariable Long clientId ,@AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.approveMatching(clientId,clientDetails.getClient()));
    }

    // 프로필 조회
    @GetMapping("/profiles")
    public ResponseEntity<SellerProfileResponseDto> getProfile(
            @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.getProfile(clientDetails.getClient()));
    }

    // 자신의 판매 상품 조회
    @GetMapping("/products")
    public ResponseEntity<Page<AllProductResponseDto>> getMyProduct(@RequestBody PageDto pageDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails ){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.getMyProduct(pageDto,clientDetails.getClient()));
    }

    //물건 판매 확정
    @PostMapping("/sell/{tradeId}")
    public ResponseEntity<String> sellProduct(@PathVariable Long tradeId,@AuthenticationPrincipal ClientDetailsImpl clientDetails){
    return ResponseEntity.status(HttpStatus.OK).body(sellerService.sellProduct(tradeId,clientDetails.getClient()));
    }

    //셀러 프로필 업데이트
    @PatchMapping("/profiles")
    public ResponseEntity<SellerProfileResponseDto> updateSellerProfile(@RequestBody SellerProfileUpdateRequestDto requestDto, @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.updateProfile(requestDto,clientDetails.getClient()));
    }


}