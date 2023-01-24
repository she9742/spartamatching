package com.example.spartamatching_01.service;


import com.example.spartamatching_01.dto.admin.*;
import com.example.spartamatching_01.dto.client.SigninResponseDto;
import com.example.spartamatching_01.dto.common.PageDto;
import com.example.spartamatching_01.dto.common.ReissueResponseDto;
import com.example.spartamatching_01.entity.*;
import com.example.spartamatching_01.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.spartamatching_01.jwt.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.example.spartamatching_01.entity.UserRoleEnum.ADMIN;
import static com.example.spartamatching_01.entity.UserRoleEnum.USER;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;
    private final ApplicantRepository applicantRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

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
    public AdminSigninResponseDto adminSignin(AdminSigninRequestDto adminSigninRequestDto, HttpServletResponse response) {

        // 사용자 확인
        Admin admin = adminRepository.findByUsername(adminSigninRequestDto.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("관리자가 존재하지 않습니다")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(adminSigninRequestDto.getPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다");
        }

        //엑세스토큰, 리프레시토큰 생성 및 반환
        String accessToken = jwtUtil.createToken(admin.getUsername(), admin.getRole());
        RefreshToken refreshToken = saveRefreshToken(admin.getUsername());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(admin.getUsername(),admin.getRole()));

        return new AdminSigninResponseDto(accessToken,refreshToken.getRefreshToken());
    }

    //리프레시 토큰 저장
    private RefreshToken saveRefreshToken(String username) {
        return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(username, jwtUtil.refreshToken(username,ADMIN),jwtUtil.getRefreshTokenTime()));
    }


    @Transactional
    public String rollbackClient(Long sellerId) {
        Client client = clientRepository.findById(sellerId).orElseThrow(
                () -> new NullPointerException("해당된 사용자가 없습니다")
        );

        if(!client.isSeller()){
            throw new IllegalArgumentException("판매자로 등록되어 있는 사용자가 아닙니다.");
        }

        client.rollbackClient();
        clientRepository.save(client);
        productRepository.deleteAllBySellerId(sellerId);
        return "판매자 권한을 제거하였습니다";
    }

    @Transactional
    public String withdraw(WithdrawPointRequestDto requestDto) {

        Client client = clientRepository.findById(requestDto.getClientId()).orElseThrow(
                () -> new NullPointerException("사용자를 찾을 수 없습니다.")
        );
        client.deposit(requestDto.getPoint());
        clientRepository.save(client);
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
    public List<Applicant> getApplySellerList() {
        List<Applicant> applicants = applicantRepository.findAll();
        return applicants;
    }

    @Transactional
    public String approveSeller(Long sellerReqId) {
        // 1. DB의 sellerReq를 확인한다.
        // 2. sellerReq를 보낸 Id의 Client를 찾는다.
        // 3. Client의 getisSeller를 true로 바꾼다.
        Applicant applicant = applicantRepository.findById(sellerReqId).orElseThrow(
                () -> new IllegalArgumentException("해당 요청을 찾을 수 없습니다.")
        );
        Client client = clientRepository.findById(applicant.getClientId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
        );
        client.updateSeller(client.getNickname(), client.getImage(), applicant);
        applicantRepository.delete(applicant);
        return "권한을 부여하였습니다.";
    }

    public Pageable makePage(PageDto pageDto) {
        Sort.Direction direction = pageDto.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, pageDto.getSortBy());
        return PageRequest.of(pageDto.getPage() - 1, pageDto.getSize(), sort);
    }


    public Admin findByAdmin(String name) {
        return adminRepository.findByUsername(name).orElseThrow();
    }


    public ReissueResponseDto reissue(String refreshToken) {
        refreshToken = resolveToken(refreshToken);
        String username = jwtUtil.getUserInfoFromToken(refreshToken).getSubject();
        RefreshToken redisRefreshToken = refreshTokenRedisRepository.findById(username).orElseThrow(NoSuchElementException::new);
        //전달받은 리프래쉬토큰이 DB에 저장된 리프레시 토큰과 같다면
        if (refreshToken.equals(resolveToken(redisRefreshToken.getRefreshToken()))) {
            return reissueRefreshToken(refreshToken, username);
        }
        throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
    }


    private ReissueResponseDto reissueRefreshToken(String refreshToken, String username) {
        //전달받은 리프래쉬토큰이 DB에 저장된 리프레시 토큰과 같으며 + 리프레시토큰의 남은시간이 리프레시토큰의 총 경과시간지났으면 새로운 리프레시토큰 생성후 저장
        if (lessThanReissueExpirationTimesLeft(refreshToken)) {
            String accessToken = jwtUtil.createToken(username, ADMIN);
            return new ReissueResponseDto(accessToken,saveRefreshToken(username).getRefreshToken());
        }
        //전달받은 리프래쉬토큰이 DB에 저장된 리프레시 토큰과 같으며 + 리프레시토큰의 남은시간이 리프레시토큰의 총 경과시간을 지나지 않았으면 리프레시토큰 그대로 재사용
        return new ReissueResponseDto(jwtUtil.createToken(username, ADMIN),refreshToken);
    }

    private boolean lessThanReissueExpirationTimesLeft(String refreshToken) {
        return jwtUtil.getRemainMilliSeconds(refreshToken) < jwtUtil.getRefreshTokenTime();
    }

    private String resolveToken(String token) {
        return token.substring(7);
    }


}



