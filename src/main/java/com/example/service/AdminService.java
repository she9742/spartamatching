package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.dto.AllClientResponseDto;
import com.example.dto.AllSellerResponseDto;
import com.example.entity.Admin;
import com.example.repository.AdminRepository;
import com.example.repository.ClientRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Client;


@Service
@RequiredArgsConstructor
public class AdminService {

	private final ClientRepository clientRepository;
	private final AdminRepository adminRepository;
  private final SellerReqRepository sellerReqRepository;
  private final ClientRepository clientRepository;


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

	//전체 고객 조회
	@Transactional(readOnly = true)
	public List<AllClientResponseDto> getClientList(){
		List<Client> clientList = clientRepository.findAll();
		List<AllClientResponseDto> clientResponseList = new ArrayList<>();
		for(Client client : clientList){
			// 판매자 조회와 다르게 ! 적용
			if (!client.getisSeller()){
				clientResponseList.add(new AllClientResponseDto(client));
			}
		}
		return clientResponseList;
	}

	// 전체 판매자 조회
	@Transactional(readOnly = true)
	public List<AllSellerResponseDto> getSellerList(){
		List<Client> sellerList = clientRepository.findAll();
		List<AllSellerResponseDto> sellerResponseList = new ArrayList<>();
		for (Client client : sellerList){
			if (client.getisSeller()){
				sellerResponseList.add(new AllSellerResponseDto(client));
			}
		}
		return sellerResponseList;
	}
  
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


}
