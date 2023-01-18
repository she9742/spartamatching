package com.example.service;

import com.example.dto.ProductRequestDto;
import com.example.dto.ProductResponseDto;
import com.example.entity.*;
import com.example.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final ProductRepository productRepository;
    private final ClientReqRepository clientReqRepository;
    private final ClientRepository clientRepository;

    private final TradeReqRepository tradeReqRepository;

    private final TalkRepository talkRepository;

    @Transactional
    public void getProfile() {

    }

    @Transactional
    public void getMyProduct() {

    }

    @Transactional
    public void getMyClientReq() {

    }

    @Transactional
    public void enrollMyProduct() {

    }

    @Transactional
    public void updateMyProduct() {

    }

    @Transactional
    public void deleteMyProduct() {

    }

    @Transactional
    public ResponseEntity<List<ClientReq>> getMatching(Long sellerId) {
        // 매칭이 들어왔는지 그러니까 매칭 요청 목록(clientReq)을 확인할 수 있다.
        //내가 셀러 아이디에 해당하는 매칭 요청 목록만 리턴해주고
        //-> 게시글 조회? 전체 게시글 조회인데 그 아이디가 셀러아이디인 경우
        List<ClientReq> clientReq = clientReqRepository.findAllBySellerId(sellerId);
        return ResponseEntity.ok().body(clientReq);

    }

    @Transactional
    public String approveMatching(ClientReq clientReq) {
        //승인해줄 클라이언트 아이디값을 넣어주고
        // 승인이 되면 클라이언트req가 사라지고 대화방이 열린다.
        //반환값은 String으로 간단하게 해주고 대화방이다
        // 대화방 열고 지운다 .
        Talk talk = new Talk(clientReq.getClientId(), clientReq.getSellerId()); // 대화방
        clientReqRepository.delete(clientReq);
        return talk.getId() + "번 대화방이 열렸습니다.";
    }

    @Transactional
    public void getTradeReq(Long sellerId) {
        List<TradeReq> tradeReqs = tradeReqRepository.findAllBySellerId(sellerId);
    }

    @Transactional
    public void sellProduct(TradeReq tradeReq, Long productId, Long talkId) {
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
        productRepository.deleteById(productId);
        tradeReqRepository.delete(tradeReq);

        // 2. 대화창을 닫는다.
        talk.closeTalk();

        // 3. point를 옮긴다.
        client.withdraw(product.getPoint());
        seller.deposit(product.getPoint());
    }

    @Transactional
    public void deposit() {
    }

    @Transactional
    public void openTalk() {
        //승인이 되는 순간 바로 대화방이 열린다.
    }

    @Transactional
    public void sendMessage() {

    }

    @Transactional
    public void getMessage() {

    }

    @Transactional
    public void closeTalk() {

    }

    // 나의 판매상품 수정
    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("수정하려는 상품이 없습니다.")
        );
        product.update(requestDto);
        return new ProductResponseDto(product);
    }


    // 판매상품 삭제
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("수정하려는 상품이 없습니다.")
        );
        product.unactivate();
    }


}
