package com.example.service;

import com.example.dto.ClientReqResponseDto;
import com.example.dto.ProductRequestDto;
import com.example.dto.ProductResponseDto;
import com.example.dto.SallerProfileUpdateRequestDto;
import com.example.entity.Client;
import com.example.entity.ClientReq;
import com.example.entity.Product;
import com.example.repository.ClientReqRepository;
import com.example.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SellerService {

    private final ProductRepository productRepository;
    private final ClientReqRepository clientReqRepository;

    @Transactional
    public String getProfile(SallerProfileUpdateRequestDto dto, Client client){
        client.updateSellerProfile(dto);
        return "변경 완료";
    }

    @Transactional
    public List<ProductResponseDto> getMyProduct(){
        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product : products){
            productResponseDtos.add(new ProductResponseDto(product));
        }
        return productResponseDtos;

    }

    @Transactional
    public List<ClientReqResponseDto> getMyClientReq(){
        List<ClientReq> clientReqs = clientReqRepository.findAll();
        List<ClientReqResponseDto> clientReqResponseDtos = new ArrayList<>();
        for(ClientReq clientReq : clientReqs){
            clientReqResponseDtos.add(new ClientReqResponseDto(clientReq));
        }
        return clientReqResponseDtos;

    }

    @Transactional
    public ProductResponseDto enrollMyProduct(ProductRequestDto dto, Client client){
        Product product = new Product(dto,client);
        return new ProductResponseDto(product);


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
    public void sellProduct(){

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

    @Transactional
    public void closeTalk(){

    }



}
