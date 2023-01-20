package com.example.controller;


import com.example.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
//@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/seller/enroll/{id}")
    public String approveSellerReq(@PathVariable Long id){
        return adminService.approveSellerReq(id);

    }

}
