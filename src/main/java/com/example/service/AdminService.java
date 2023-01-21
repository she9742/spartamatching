package com.example.service;


import com.example.dto.*;
import com.example.entity.Admin;
import com.example.entity.Client;
import com.example.entity.SellerReq;
import com.example.repository.AdminRepository;
import com.example.repository.ClientRepository;
import com.example.repository.ProductRepository;
import com.example.repository.SellerReqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;
    private final SellerReqRepository sellerReqRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;

    @Transactional
    public String adminSignup(AdminSignupRequestDto adminSignupRequestDto) {

        //비밀번호 인코드
        String password = passwordEncoder.encode(adminSignupRequestDto.getPassword());
        //회원 중복 확인
        Optional<Admin> Admins = adminRepository.findByUsername(adminSignupRequestDto.getUsername());
        if (Admins.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 관리자입니다.");
        }

        Admin admin = new Admin(adminSignupRequestDto, password);
        adminRepository.save(admin);

        return "가입 완료";
    }


    @Transactional
    public String adminSignin(AdminSigninRequestDto adminSigninRequestDto) {

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


    public String rollbackClient(Long sellerId) {
        Client client = clientRepository.findById(sellerId).orElseThrow(
                () -> new NullPointerException("해당된 사용자가 없습니다")
        );

        if(!client.getisSeller()){
            throw new IllegalArgumentException("판매자로 등록되어 있는 사용자가 아닙니다.");
        }

        client.rollbackClient();
        productRepository.deleteAllBySellerId(sellerId);

        return "판매자 권한을 제거하였습니다";
    }

    public String withdraw(WithdrawPointRequestDto requestDto) {

        Client client = clientRepository.findById(requestDto.getClientId()).orElseThrow(
                () -> new NullPointerException("사용자를 찾을 수 없습니다.")
        );
        client.deposit(requestDto.getPoint());
        return "포인트를 지급했습니다.";
    }

    @Transactional(readOnly = true)
    public Page<AllClientResponseDto> getClientList(PageDto pageDto)  {
        Pageable pageable=makePage(pageDto);

        Page<Client> clientList = clientRepository.findAll(pageable);
        Page<AllClientResponseDto> clientResponseList = clientList.map(AllClientResponseDto::new);
        return clientResponseList;
    }


    @Transactional
    public List<SellerReq> getApplySellerList() {
        List<SellerReq> sellerReqs = sellerReqRepository.findAll();
        return sellerReqs;
    }

    @Transactional
    public String approveSellerReq(Long sellerReqId) {
        // 1. DB의 sellerReq를 확인한다.
        // 2. sellerReq를 보낸 Id의 Client를 찾는다.
        // 3. Client의 getisSeller를 true로 바꾼다.
        SellerReq sellerReq = sellerReqRepository.findByClientId(sellerReqId).orElseThrow(
                () -> new IllegalArgumentException("해당 요청을 찾을 수 없습니다.")
        );
        Client client = clientRepository.findById(sellerReq.getClientId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
        );
        client.updateSeller(client.getNickname(), client.getImage(), sellerReq);

        return "권한을 부여하였습니다.";
    }

    public Pageable makePage(PageDto pageDto) {
        Sort.Direction direction = pageDto.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, pageDto.getSortBy());
        return PageRequest.of(pageDto.getPage() - 1, pageDto.getSize(), sort);
    }
}



