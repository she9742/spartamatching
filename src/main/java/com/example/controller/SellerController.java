package com.example.controller;


import com.example.entity.ClientReq;
import com.example.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seller")
public class SellerController {


    private final SellerService sellerService;

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

}
