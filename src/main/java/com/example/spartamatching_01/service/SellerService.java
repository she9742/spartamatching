package com.example.spartamatching_01.service;


import com.example.spartamatching_01.dto.common.AllProductResponseDto;
import com.example.spartamatching_01.dto.common.PageDto;
import com.example.spartamatching_01.dto.common.ProductResponseDto;
import com.example.spartamatching_01.dto.seller.ApplicantResponseDto;
import com.example.spartamatching_01.dto.seller.ProductRequestDto;
import com.example.spartamatching_01.dto.seller.SellerProfileResponseDto;
import com.example.spartamatching_01.dto.seller.SellerProfileUpdateRequestDto;
import com.example.spartamatching_01.entity.*;
import com.example.spartamatching_01.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final ProductRepository productRepository;
    private final MatchingRepository matchingRepository;
    private final ClientRepository clientRepository;
    private final TradeRepository tradeRepository;
    private final TalkRepository talkRepository;

    @Transactional //판매자 프로필 조회
    public SellerProfileResponseDto getProfile(Client client){
        return new SellerProfileResponseDto(client);
    }

    @Transactional  //판매자 프로필 수정
    public SellerProfileResponseDto updateProfile(SellerProfileUpdateRequestDto dto, Client client){
        client.updateSellerProfile(dto);
        clientRepository.save(client);
        return new SellerProfileResponseDto(client);
    }

    @Transactional
    public Page<AllProductResponseDto> getMyProduct(PageDto pageDto, Client seller) {
        Pageable pageable = makePage(pageDto);
        Page<Product> products = productRepository.findBySellerId(pageable,seller.getId());
        Page<AllProductResponseDto> allProductResponseDtos = products.map(AllProductResponseDto::new);
        return allProductResponseDtos;
    }




    //자신의 상품 등록
    @Transactional
    public ProductResponseDto enrollMyProduct(ProductRequestDto dto, Client seller){
        if(!seller.isSeller()){
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
    public Page<ApplicantResponseDto> getMatching(PageDto pageDto, Client seller){
        Pageable pageable = makePage(pageDto);
        Page<Matching> clientReqs = matchingRepository.findAllBySellerId(pageable,seller.getId());
        Page<ApplicantResponseDto> clientResponseDtos = clientReqs.map(ApplicantResponseDto::new);
        return clientResponseDtos;
    }


    @Transactional
    public String approveMatching(Long clientReqId, Client seller) {
        Matching matching = matchingRepository.findById(clientReqId).orElseThrow(
                () -> new NullPointerException("매칭 정보를 찾을수 없습니다.")
        );
        Product product = productRepository.findById(matching.getProductId()).orElseThrow(
                () -> new NullPointerException("해당 상품이 존재하지 않습니다.")
        );
        if (!product.getSellerId().equals(seller.getId())){
            throw new IllegalArgumentException("자신의 거래가 아닙니다");
        }
        Talk talk = new Talk(matching.getClientId(), product.getId()); // 대화방
        talkRepository.save(talk);
        matchingRepository.delete(matching);
        return talk.getId() + "번 대화방이 열렸습니다.";
    }

    @Transactional
    public List<Trade> getTradeList(Client seller) {
        List<Trade> trade = tradeRepository.findAllBySellerId(seller.getId());
        return trade;
    }


    @Transactional
    public String sellProduct(Long tradeReqId,Client seller) {
        Trade trade = tradeRepository.findById(tradeReqId).orElseThrow(
                () -> new IllegalArgumentException("거래신청이 존재하지 않습니다")
        );

        if (!trade.getSellerId().equals(seller.getId())){
            throw new IllegalArgumentException("본인의 거래가 아닙니다.");
        }

        Client client = clientRepository.findById(trade.getClientId()).orElseThrow(
                () -> new IllegalArgumentException("해당 구매자가 존재하지 않습니다.")
        );
        Client sellers = clientRepository.findById(trade.getSellerId()).orElseThrow(
                () -> new IllegalArgumentException("해당 판매자가 존재하지 않습니다.")
        );
        Product product = productRepository.findById(trade.getProductId()).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );

        if(!product.getActivation()){
            new IllegalArgumentException("판매중인 상품이 아닙니다.");
        }

        Talk talk = talkRepository.findByClientIdAndProductId(client.getId(), product.getId()).orElseThrow(
                () -> new IllegalArgumentException("거래중인 대상이 올바르지 않습니다")
        );

        if(!talk.isActivation()){
            new IllegalArgumentException("활성화된 거래방이 아닙니다.");
        }


        //물건이 판매되면 product,tradeReq 를 삭제한다.(비활성화)
        product.unactivate();
        tradeRepository.delete(trade);
        talk.closeTalk();

        if (client.getPoint() < product.getPoint()) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        } else {
            client.withdraw(product.getPoint());
            sellers.deposit(product.getPoint());
        }
        return "거래가 완료되었습니다.";
    }


    public Pageable makePage(PageDto pageDto) {
        Sort.Direction direction = pageDto.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, pageDto.getSortBy());
        return PageRequest.of(pageDto.getPage() - 1, pageDto.getSize(), sort);
    }

}