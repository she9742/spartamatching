package com.example.service;

import com.example.entity.Client;
import com.example.entity.Product;
import com.example.entity.Talk;
import com.example.entity.TradeReq;
import com.example.repository.ClientRepository;
import com.example.repository.ProductRepository;
import com.example.repository.TalkRepository;
import com.example.repository.TradeReqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final ClientRepository clientRepository;

    private final ProductRepository productRepository;

    private final TradeReqRepository tradeReqRepository;

    private final TalkRepository talkRepository;

    @Transactional
    public void getProfile(){

    }

    @Transactional
    public void getMyProduct(){

    }

    @Transactional
    public void getMyClientReq(){

    }

    @Transactional
    public void enrollMyProduct(){

    }

    @Transactional
    public void updateMyProduct(){

    }

    @Transactional
    public void deleteMyProduct(){

    }

    @Transactional
    public void approveMatching(){

    }

    @Transactional
    public void getTradeReq(Long sellerId){
        List<TradeReq> tradeReqs = tradeReqRepository.findAllBySellerId(sellerId);
    }

    @Transactional
    public void sellProduct(TradeReq tradeReq, Long productId, Long talkId){
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
    public void deposit(){

    }

    @Transactional
    public void openTalk(){

    }

    @Transactional
    public void sendMessage(){

    }

    @Transactional
    public void getMessage(){

    }

}
