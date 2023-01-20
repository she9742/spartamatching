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
    public List<ProductResponseDto> getMyProduct(Long sellerId){
        List<Product> products = productRepository.findBySellerId(sellerId);
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product : products){
            productResponseDtos.add(new ProductResponseDto(product));
        }
        return productResponseDtos;

    }

    @Transactional
    public List<ClientReqResponseDto> getMyClientReq(Long sellerId){
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
    public ResponseEntity<List<ClientReq>> getMatching(Long sellerId) {
        List<ClientReq> clientReq = clientReqRepository.findAllBySellerId(sellerId);
        return ResponseEntity.ok().body(clientReq);

    }

    @Transactional
    public String approveMatching(Long ClientReqId) {
        ClientReq clientReq = clientReqRepository.findById(ClientReqId).orElseThrow(
                () -> new NullPointerException("")
        );
        Talk talk = new Talk(clientReq.getClientId(), clientReq.getSellerId()); // 대화방
        clientReqRepository.delete(clientReq);
        return talk.getId() + "번 대화방이 열렸습니다.";
    }

    @Transactional
    public ResponseEntity<List<TradeReq>> getTradeReq(Long sellerId) {
        List<TradeReq> tradeReqs = tradeReqRepository.findAllBySellerId(sellerId);
        return ResponseEntity.ok().body(tradeReqs);
    }

    @Transactional
    public String sellProduct(TradeReq tradeReq, Long productId, Long talkId) {
        Client client = clientRepository.findById(tradeReq.getClientId()).orElseThrow(
                () -> new IllegalArgumentException("")
        );
        Client seller = clientRepository.findById(tradeReq.getSellerId()).orElseThrow(
                () -> new IllegalArgumentException("해당 판매자가 존재하지 않습니다.")
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        Talk talk = talkRepository.findById(talkId).orElseThrow(
                () -> new IllegalArgumentException("")
        );
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
            seller.deposit(product.getPoint());
        }
        return "거래가 완료되었습니다.";
    }


}