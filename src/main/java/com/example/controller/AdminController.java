package com.example.controller;

import com.example.dto.*;
import com.example.entity.SellerReq;
import com.example.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;


    // 전체 고객 목록 조회
    @GetMapping("/client")
    public ResponseEntity<Page<AllClientResponseDto>> getClientList(@RequestBody PageDto pageDto) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getClientList(pageDto));

    }


    @GetMapping("/seller/request")
    public ResponseEntity<List<SellerReq>> getApplySellerList() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getApplySellerList());
    }

    @PostMapping("/signup")
    public ResponseEntity<String> adminSignup(@RequestBody AdminSignupRequestDto adminSignupRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.adminSignup(adminSignupRequestDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<String> adminSignin(@RequestBody AdminSigninRequestDto adminSigninRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.adminSignin(adminSigninRequestDto));
    }

    //판매자 권한 삭제
    @PutMapping("/seller/disenroll/{id}")
    public ResponseEntity<String> rollbackClient(@PathVariable Long sellerId) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.rollbackClient(sellerId));
    }

    //포인트 부여
    @PostMapping("/givepoint")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawPointRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.withdraw(requestDto));
    }


    @PostMapping("/seller/enroll/{id}")
    public ResponseEntity<String> approveSellerReq(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(adminService.approveSellerReq(id));

    }
}