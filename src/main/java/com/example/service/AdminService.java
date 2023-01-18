package com.example.service;

import com.example.entity.Admin;
import com.example.repository.AdminRepository;
import com.example.repository.ClientRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.example.entity.Client;


@Service
@RequiredArgsConstructor
public class AdminService {
    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;


    public String rollbackClient(Long sellerId){
        Client client = clientRepository.findById(sellerId).orElseThrow(
                () -> new NullPointerException("")
        );
        client.rollbackClient();

        return "판매자 권한을 박탈당하셨습니다.";
    }

    public String withdraw(int point,Long adminId, Long clientId){

        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new NullPointerException("사용자를 찾을 수 없습니다.")
        );
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new NullPointerException("해당 관리자를 찾을 수 없습니다.")
        );
        if(point > admin.getPoint()){
            throw new IllegalArgumentException("관리자의 포인트가 부족합니다.");
        } else{
            admin.withdraw(point);
            client.deposit(point);
        }
        return "포인트를 지급했습니다.";
    }

}
