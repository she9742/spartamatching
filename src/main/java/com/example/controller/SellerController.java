package com.example.controller;

import com.example.dto.ClientReqResponseDto;
import com.example.dto.ProductRequestDto;
import com.example.dto.ProductResponseDto;
import com.example.dto.SellerProfileResponseDto;
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
    ResponseEntity<ProductResponseDto> enrollMyProdcut(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ProductResponseDto productResponseDto = sellerService.enrollMyProduct(requestDto,userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDto);
    }
    //판매 상품 수정
    @PatchMapping("/products/{id}")
    ResponseEntity<ProductResponseDto> updateMyProduct(@PathVariable Long id, @RequestBody ProductRequestDto productRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ProductResponseDto productResponseDtos = sellerService.updateMyProduct(id, productRequestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDtos);
    }

    //판매 상품 삭제
    @PutMapping("/products/{id}")
    public ResponseEntity<String> deleteMyProduct(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return sellerService.deleteMyProduct(id,userDetails.getUser());
    }
    
    // 고객의 요청 목록을 조회
    @GetMapping("/clientLists")
    public ResponseEntity<List<ClientReq>> getMatching(@AuthenticationPrincipal SellerDetailsImpl sellerDetails){
        return sellerService.getMatching(sellerDetails.getSeller());
    }

    // 고객의 요청을 처리
    @PostMapping("/clients/{clientReqId}")
    public String approveMatching(@PathVariable Long clientReqId ,@AuthenticationPrincipal SellerDetailsImpl sellerDetails){
        return sellerService.approveMatching(sellerDetails.getSeller());
    }

    // 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<SellerProfileResponseDto> getProfile(
        @AuthenticationPrincipal ClientDetailsImpl clientDetails) {
        return sellerService.getProfile(clientDetails.getClient());
    }
    // 판매 상품 조회
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getMyProduct(@PathVariable Long sellerId,@AuthenticationPrincipal ClientDetailsImpl clientDetails ){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.getMyProduct(sellerId,clientDetails.getClient()));
    }

    // 고객 요청 목록 조회
    @GetMapping("/clientLists")
    public ResponseEntity<List<ClientReqResponseDto>> getMyClientReq(@PathVariable Long sellerID,@AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.getMyClientReq(sellerID,clientDetails.getClient()));
    }

}
