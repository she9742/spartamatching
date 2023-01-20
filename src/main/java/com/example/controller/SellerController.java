package com.example.controller;

import com.example.dto.ProductRequestDto;
import com.example.dto.ProductResponseDto;
import com.example.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}
