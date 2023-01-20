package com.example.controller;


import com.example.entity.Client;
import com.example.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seller")
//@SecurityRequirement(name = "Bearer Authentication")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/sell/{tradereqid}")
    public String sellProduct(@PathVariable Long tradereqid){
        return sellerService.sellProduct(tradereqid);
    }



}
