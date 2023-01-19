package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.dto.AdminSigninRequestDto;
import com.example.dto.AdminSignupRequestDto;
import com.example.dto.AllClientResponseDto;
import com.example.dto.AllSellerResponseDto;
import com.example.entity.Admin;
import com.example.entity.SellerReq;
import com.example.repository.AdminRepository;
import com.example.repository.ClientRepository;
import com.example.repository.SellerReqRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Client;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;
    private final SellerReqRepository sellerReqRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String adminSignup(AdminSignupRequestDto adminSignupRequestDto){

        //비밀번호 인코드
        String password = passwordEncoder.encode(adminSignupRequestDto.getPassword());
        //회원 중복 확인
        Optional<Admin> Admins = adminRepository.findByUsername(adminSignupRequestDto.getUsername());
        if (Admins.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 관리자입니다.");
        }

        Admin admin = new Admin(adminSignupRequestDto,password);
        adminRepository.save(admin);

        return "가입 완료";
    }


    @Transactional
    public String Adminsignin(AdminSigninRequestDto adminSigninRequestDto){

        //복호화 추가해야함

        // 사용자 확인
        Admin admin = adminRepository.findByUsername(adminSigninRequestDto.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("관리자가 존재하지 않습니다")
        );
        // 비밀번호 확인
        //스프링 시큐리티 내부기능사용 입력된 비밀번호와 저장된 비밀번호 비교
        if (!passwordEncoder.matches(adminSigninRequestDto.getPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다");
        }


        //리프레쉬토큰,엑세스토큰 연동필요
//        String accessToken = jwtUtil.createToken(admin.getUsername(), admin.권한());
//        String refreshToken1 = jwtUtil.refreshToken(admin.getUsername(), admin.권한());
//        return new TokenResponseDto(accessToken, refreshToken1);

        return "로그인 완료";

    }


    public String rollbackClient(Long sellerId){
        Client client = clientRepository.findById(sellerId).orElseThrow(
                () -> new NullPointerException("해당된 사용자가 없습니다")
        );
        client.rollbackClient();
        //DB에서 자신에게 연결된 상품들 찾아서 없앰

        return "판매자 권한을 박탈당하셨습니다.";
    }

    public String withdraw(int point,Long adminId, Long clientId){
//public String withdraw(int point, Long clientId dto,Admin admin 로 묶어서 가저옴){
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new NullPointerException("사용자를 찾을 수 없습니다.")
        );
//        Admin admin = adminRepository.findById(adminId).orElseThrow(
//                () -> new NullPointerException("해당 관리자를 찾을 수 없습니다.")
//        );
//        if(point > admin.getPoint()){
//            throw new IllegalArgumentException("관리자의 포인트가 부족합니다.");
//        } else{
            //admin.withdraw(point);
            client.deposit(point);
        //}
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
    public ResponseEntity<List<SellerReq>> getApplySellerList(){
        List<SellerReq> sellerReqs = sellerReqRepository.findAll();
        return ResponseEntity.ok().body(sellerReqs);
    }

    @Transactional
        //sellerReq받아서 처리하는걸로
    public void approveSellerReq(Long clientId){
//        Client client = clientRepository.findById(clientId).orElseThrow(
//                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
//        );
//        SellerReq sellerReq = sellerReqRepository.findById(client.getId()).orElseThrow(
//                () -> new IllegalArgumentException("판매자 요청을 하지 않은 시용자입니다.")
//        );
//        if (Objects.equals(client.getId(), sellerReq.getId())) client.getisSeller();

    }

}



