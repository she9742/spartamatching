package com.example.controller;

import com.example.security.ClientDetailsImpl;
import com.example.dto.ProductRequestDto;
import com.example.dto.ProductResponseDto;
import com.example.dto.SellerProfileResponseDto;
import com.example.entity.TradeReq;
import com.example.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import com.example.entity.ClientReq;
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
    ResponseEntity<ProductResponseDto> enrollMyProdcut(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal ClientDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerService.enrollMyProduct(requestDto, userDetails.getClient()));
    }
    //판매 상품 수정
    @PatchMapping("/products/{id}")
    ResponseEntity<ProductResponseDto> updateMyProduct(@PathVariable Long id, @RequestBody ProductRequestDto productRequestDto, @AuthenticationPrincipal ClientDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.updateMyProduct(id,productRequestDto,userDetails.getClient()));
    }

    //판매 상품 삭제
    @PutMapping("/products/{id}")
    public ResponseEntity<String> deleteMyProduct(@PathVariable Long id, @AuthenticationPrincipal ClientDetailsImpl userDetails){
        return sellerService.deleteMyProduct(id,userDetails.getClient());
    }

    // 고객의 매칭요청 목록을 조회
    @GetMapping("/clientLists")
    public ResponseEntity<List<ClientReq>> getMatching(@AuthenticationPrincipal ClientDetailsImpl sellerDetails){
        return sellerService.getMatching(sellerDetails.getClient());
    }

    // 고객의 거래요청 목록을 조회
    @GetMapping("/tradeLists")
    public ResponseEntity<List<TradeReq>> getTradeReq(@AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.getTradeReq(clientDetails.getClient()));
    }

    // 고객의 요청을 처리
    @PostMapping("/clients/{clientReqId}")
    public String approveMatching(@PathVariable Long clientReqId ,@AuthenticationPrincipal ClientDetailsImpl sellerDetails){
        return sellerService.approveMatching(clientReqId,sellerDetails.getClient());
    }

    // 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<SellerProfileResponseDto> getProfile(
            @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return sellerService.getProfile(clientDetails.getClient());
    }
    // 판매 상품 조회
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getMyProduct(@AuthenticationPrincipal ClientDetailsImpl clientDetails ){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.getMyProduct(clientDetails.getClient()));
    }

    @PostMapping("/sell/{tradereqid}")
    public String sellProduct(@PathVariable Long tradereqid,@AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return sellerService.sellProduct(tradereqid,clientDetails.getClient());
    }


}