package com.example.service;

import com.example.entity.Client;
import com.example.entity.SellerReq;
import com.example.repository.ClientRepository;
import com.example.repository.SellerReqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final SellerReqRepository sellerReqRepository;

    private final ClientRepository clientRepository;

    @Transactional
    public ResponseEntity<List<SellerReq>> getApplySellerList(Long clientId){
        List<SellerReq> sellerReqs = sellerReqRepository.findAllByClientId(clientId);
        return ResponseEntity.ok().body(sellerReqs);
    }

    @Transactional
    public void approveSellerReq(Long clientId){
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );
        SellerReq sellerReq = sellerReqRepository.findById(client.getId()).orElseThrow(
                () -> new IllegalArgumentException("판매자 요청을 하지 않은 시용자입니다.")
        );
        if (Objects.equals(client.getId(), sellerReq.getId())) client.getisSeller();

    }

}
