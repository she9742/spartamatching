package com.example.service;

import com.example.dto.*;
import com.example.entity.*;
import com.example.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final ProductRepository productRepository;
    private final ClientReqRepository clientReqRepository;
    private final ClientRepository clientRepository;
    private final TradeReqRepository tradeReqRepository;
    private final TalkRepository talkRepository;

    //판매자 프로필 조회
    public SellerProfileResponseDto getProfile(Client client){
        return new SellerProfileResponseDto(client);
    }

    @Transactional  //판매자 프로필 수정
    public String updateProfile(SellerProfileUpdateRequestDto dto, Client client){
        client.updateSellerProfile(dto);
        return "변경 완료";
    }

    @Transactional
    public List<ProductResponseDto> getMyProduct(Client seller){
        List<Product> products = productRepository.findBySellerId(seller.getId());
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product : products){
            productResponseDtos.add(new ProductResponseDto(product));
        }
        return productResponseDtos;

    }

    @Transactional
    public List<ClientReqResponseDto> getMyClientReq(Long sellerId, Client seller){
        if (!sellerId.equals(seller.getId())){
            throw new IllegalArgumentException("조회할 권한이 없습니다.");
        }
        List<ClientReq> clientReqs = clientReqRepository.findAllBySellerId(sellerId);
        List<ClientReqResponseDto> clientReqResponseDtos = new ArrayList<>();
        for(ClientReq clientReq : clientReqs){
            clientReqResponseDtos.add(new ClientReqResponseDto(clientReq));
        }
        return clientReqResponseDtos;

    }

    @Transactional
    public ProductResponseDto enrollMyProduct(ProductRequestDto dto, Client seller){
        if(!seller.getisSeller()){
            throw new IllegalArgumentException("상품을 등록할 권한이 없습니다.");
        }
        Product product = new Product(dto,seller);
        productRepository.save(product);
        return new ProductResponseDto(product);


    }

    // 나의 판매상품 수정
    @Transactional
    public ProductResponseDto updateMyProduct(Long id, ProductRequestDto requestDto, Client seller) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("수정하려는 상품이 없습니다.")
        );
        if(!product.getSellerId().equals(seller.getId())){
            throw new IllegalArgumentException("상품을 수정할 권한이 없습니다.");
        }
        product.update(requestDto);
        return new ProductResponseDto(product);
    }


    // 판매상품 삭제
    @Transactional
    public String deleteMyProduct(Long id, Client seller) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("삭제하려는 상품이 없습니다.")
        );
        if(!product.getSellerId().equals(seller.getId())){
            throw new IllegalArgumentException("상품을 수정할 권한이 없습니다.");
        }
        product.unactivate();
        return "상품이 삭제되었습니다.";
    }

    @Transactional
    public ResponseEntity<List<ClientReq>> getMatching(Client seller){
        List<ClientReq> clientReq = clientReqRepository.findAllBySellerId(seller.getId());
        return ResponseEntity.ok().body(clientReq);
    }

    @Transactional
    public String approveMatching(Long clientReqId, Client seller) {
        ClientReq clientReq = clientReqRepository.findById(clientReqId).orElseThrow(
                () -> new NullPointerException("매칭 정보를 찾을수 없습니다.")
        );
        if (!clientReq.getSellerId().equals(seller.getId())){
            throw new IllegalArgumentException("자신의 거래가 아닙니다");
        }
        Talk talk = new Talk(clientReq.getClientId(), clientReq.getSellerId()); // 대화방
        talkRepository.save(talk);
        clientReqRepository.delete(clientReq);
        return talk.getId() + "번 대화방이 열렸습니다.";
    }

    @Transactional
    public ResponseEntity<List<TradeReq>> getTradeReq(Long sellerId) {
        List<TradeReq> tradeReqs = tradeReqRepository.findAllBySellerId(sellerId);
        return ResponseEntity.ok().body(tradeReqs);
    }

    @Transactional
    //TradeReq tradeReq 를통째로 보내는 상황이 없음 + talkId를 컨트롤러에서 보내야한다?
    //public String sellProduct(TradeReq tradeReq, Long talkId) {
    public String sellProduct(Long tradeReqId,Client seller) {

        //회원삭제가 없기때문에 회원이 존재하지 않을 확률은 없으나, 삭제기능(비활성화기능)도입을 고려하여 설계함
        //->만약 삭제기능(비활성화기능)을 추가한다면 아래검색로직이후 활성화된 계정인지 체크하는 로직필요

        //지나치게 DB접근 자주함. 성능개선을위해 회원이 없어지거나 상품이 비활성화될때 관련된 예약데이터를 모두 지우는 방식으로 변경가능
        TradeReq tradeReq = tradeReqRepository.findById(tradeReqId).orElseThrow(
                () -> new IllegalArgumentException("거래신청이 존재하지 않습니다")
        );

        //보안체크
        if (!tradeReq.getSellerId().equals(seller.getId())){
            throw new IllegalArgumentException("본인의 거래가 아닙니다.");
        }


        Client client = clientRepository.findById(tradeReq.getClientId()).orElseThrow(
                () -> new IllegalArgumentException("해당 구매자가 존재하지 않습니다.")
        );
        Client sellers = clientRepository.findById(tradeReq.getSellerId()).orElseThrow(
                () -> new IllegalArgumentException("해당 판매자가 존재하지 않습니다.")
        );
        Product product = productRepository.findById(tradeReq.getProduectId()).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );

        //해당 상품이 활성화된 상품인지 체크
        if(!product.getActivation()){
            new IllegalArgumentException("판매중인 상품이 아닙니다.");
        }


//        Talk talk = talkRepository.findById(talkId).orElseThrow(
//                () -> new IllegalArgumentException("")
//        );
        Talk talk = talkRepository.findByClientIdAndSellerId(client.getId(), sellers.getId()).orElseThrow(
                () -> new IllegalArgumentException("거래중인 대상이 올바르지 않습니다")
        );
        //해당 거래방이 활성화된 거래방인지 체크
        if(!talk.isActivation()){
            new IllegalArgumentException("활성화된 거래방이 아닙니다.");
        }


        // 1. 물건이 판매되면 product,tradeReq 를 삭제한다.
        product.unactivate();   //삭제가 아니라 비활성화로 변경
        tradeReqRepository.delete(tradeReq);

        // 2. 대화창을 닫는다.
        talk.closeTalk();

        // 3. point를 옮긴다.   //혹시 돈이부족하다면?
        if (client.getPoint() < product.getPoint()) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        } else {
            client.withdraw(product.getPoint());
            sellers.deposit(product.getPoint());
        }
        return "거래가 완료되었습니다.";
    }


}