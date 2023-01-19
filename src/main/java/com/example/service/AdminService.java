package com.example.service;


import com.example.dto.AdminSigninRequestDto;
import com.example.dto.AdminSignupRequestDto;
import com.example.dto.SigninRequestDto;
import com.example.dto.SignupRequestDto;
import com.example.entity.Admin;
import com.example.entity.Client;
import com.example.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
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


}
