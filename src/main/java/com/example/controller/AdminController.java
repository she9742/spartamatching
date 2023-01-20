package com.example.controller;

import com.example.dto.AllClientResponseDto;
import com.example.dto.AllSellerResponseDto;
import com.example.entity.SellerReq;
import com.example.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    // 전체 고객 목록 조회
    @GetMapping("/client")
    public List<AllClientResponseDto> getClientList(@AuthenticationPrincipal ClientDetailsImpl clientDetails){
        return adminService.getClientList(clientDetails.getClinet());
    }


    // 전체 판매자 조회
    // Client에도 똑같은 기능이 있는데 굳이 Admin에 필요할까????
    // ClientService 와 AdminService 의 코드가 완전히 동일함
    @GetMapping("/seller")
    public List<AllSellerResponseDto> getSellerList(){
        return adminService.getSellerList();
    }




    @GetMapping("/seller/request")
    public ResponseEntity<List<SellerReq>> getApplySellerList(){
        return adminService.getApplySellerList();
    }




}
