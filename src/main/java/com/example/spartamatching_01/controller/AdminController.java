package com.example.spartamatching_01.controller;

import com.example.spartamatching_01.dto.*;
import com.example.spartamatching_01.entity.Admin;
import com.example.spartamatching_01.entity.SellerReq;
import com.example.spartamatching_01.jwt.JwtUtil;
import com.example.spartamatching_01.security.AdminDetailsImpl;
import com.example.spartamatching_01.service.AdminService;
import com.example.spartamatching_01.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ClientService clientService;
    private final AdminService adminService;
    private final JwtUtil jwtUtil;

    // 전체 고객 목록 조회

    @Secured("ROLE_ADMIN")
    @GetMapping("/client")
    public ResponseEntity<Page<AllClientResponseDto>> getClientList(@RequestBody PageDto pageDto,@AuthenticationPrincipal AdminDetailsImpl adminDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getClientList(pageDto));

    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/seller/request")
    public ResponseEntity<List<SellerReq>> getApplySellerList() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getApplySellerList());
    }

    @PostMapping("/signup")
    public ResponseEntity<String> adminSignup(@RequestBody AdminSignupRequestDto adminSignupRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.adminSignup(adminSignupRequestDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<AdminMessageResponseDto> adminSignin(@RequestBody AdminSigninRequestDto adminSigninRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.adminSignin(adminSigninRequestDto));
    }

    //판매자 권한 삭제
    @Secured("ROLE_ADMIN")
    @PutMapping("/seller/disenroll/{id}")
    public ResponseEntity<String> rollbackClient(@PathVariable Long sellerId) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.rollbackClient(sellerId));
    }

    //포인트 부여
    @Secured("ROLE_ADMIN")
    @PostMapping("/givepoint")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawPointRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.withdraw(requestDto));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/seller/enroll/{id}")
    public ResponseEntity<String> approveSellerReq(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(adminService.approveSellerReq(id));

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/refresh")
    public TokenResponseDto adminRefresh(HttpServletRequest request, @RequestBody TokenRequestDto tokenRequestDto){
        //bearer 제거
        String resolvedAccessToken = jwtUtil.resolveAccessToken(tokenRequestDto.getAccessToken());

        //Access 토큰 username가져오기
        //인증을 확인하고 authenticationAccessToken 변수에 토큰저장
        Authentication authenticationAccessToken = jwtUtil.getAuthentication(resolvedAccessToken);
        //DB에 접근하여 위에서만든 토큰에 해당하는 유저의 정보 반환
        Admin accessUser = adminService.findByAdmin(authenticationAccessToken.getName());

        //Refrest 토큰 username가져오기
        String refreshToken = request.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER);
        String resolvedRefreshToken = jwtUtil.resolveRefreshToken(refreshToken);
        //인증을 확인하고 authenticationFreshToken 변수에 토큰저장
        Authentication authenticationFreshToken = jwtUtil.getAuthentication(resolvedRefreshToken);
        //DB에 접근하여 위에서만든 토큰에 해당하는 유저의 정보 반환
        Admin refreshUser = adminService.findByAdmin(authenticationFreshToken.getName());

        //두개 비교 후 맞으면 재발행 ->엑세스토큰과 리프레시토큰의 유저정보가 같은지 확인하는작업
        if (accessUser == refreshUser) {
            return clientService.reissue(refreshUser.getUsername(), refreshUser.getRole());
        }
        throw new IllegalStateException("리프레시 토큰과 엑세스토큰의 사용자가 일치하지 않습니다.");
    }

}