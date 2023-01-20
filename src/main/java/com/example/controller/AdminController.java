package com.example.controller;

import com.example.dto.AdminSigninRequestDto;
import com.example.dto.AdminSignupRequestDto;
import com.example.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<String> adminSignup(@RequestBody AdminSignupRequestDto adminSignupRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(adminService.adminSignup(adminSignupRequestDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<String> adminSignin(@RequestBody AdminSigninRequestDto adminSigninRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(adminService.adminSignin(adminSigninRequestDto));
    }
}
