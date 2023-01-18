package com.example.service;


import com.example.entity.Client;
import com.example.entity.SellerReq;
import com.example.repository.ClientRepository;
import com.example.repository.SellerReqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ClientService {

    private final SellerReqRepository sellerReqRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public ResponseEntity<String> applySeller(Long clientId){


        //현재 판매자 등록 요청이 있는지 확인한다
        Optional<SellerReq> client = sellerReqRepository.findByClientId(clientId);
        if(client.isPresent()){
            //있다면 오류메시지를 띄우고 취소시킨다
            throw new IllegalArgumentException("이미 신청한 유저입니다.");
        }
        //이미 판매자인지 확인한다
            //1.db에 다시 접근해서 확인
        Client isSeller = clientRepository.findById(clientId).orElseThrow(
                () -> new IllegalArgumentException("이미 판매자입니다.")
        );
            //2.컨트롤러에서 userDetails를 사용하여 확인후 메소드 진입
                //->컨트롤러 생성후 이방식으로 변경


        //없다면 DB에 등록 요청을 등록한다
        SellerReq sellerReq = new SellerReq(clientId);
        sellerReqRepository.save(sellerReq);

        return new ResponseEntity<>("판매자 신청을 하였습니다.", HttpStatus.OK);
    }


}
