package com.example.controller;

import com.example.dto.*;
import com.example.entity.SellerReq;
import com.example.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;


    // 전체 고객 목록 조회
    @GetMapping("/client")
    public List<AllClientResponseDto> getClientList() {
        return adminService.getClientList();
    }


    //클라이언트컨트롤러의 기능과 100%겹치므로 지워도 될듯하다. -> 추후에 삭제
    // 전체 판매자 조회
    // Client에도 똑같은 기능이 있는데 굳이 Admin에 필요할까????
    // ClientService 와 AdminService 의 코드가 완전히 동일함
//    @GetMapping("/seller")
//    public List<AllSellerResponseDto> getSellerList() {
//        return adminService.getSellerList();
//    }


    @GetMapping("/seller/request")
    public ResponseEntity<List<SellerReq>> getApplySellerList() {
        return adminService.getApplySellerList();
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
    public String approveSellerReq(@PathVariable Long id){
        return adminService.approveSellerReq(id);

    }
}