package com.example.controller;

import com.example.dto.AllProductResponseDto;
import com.example.dto.AllSellerResponseDto;
import com.example.dto.ProductResponseDto;
import com.example.dto.SellerResponseDto;
import com.example.service.ClientService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    //전체 판매 상품 조회
    @GetMapping("/client/prodcuts")
    public ResponseEntity<List<AllProductResponseDto>> getAllProduct(){
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllProducts());
    }

    //전체 판매자 조회
    @GetMapping("/client/sellers")
    public ResponseEntity<List<AllSellerResponseDto>> getAllSellers(){
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllSellers());
    }

    //판매자 선택 조회
    @GetMapping("/client/seller/{id}")
    public ResponseEntity<SellerResponseDto> getSellerInfo(@PathVariable Long sellerId){
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getSellerInfo(sellerId));
    }
}
