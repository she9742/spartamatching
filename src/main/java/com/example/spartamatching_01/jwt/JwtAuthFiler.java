package com.example.spartamatching_01.jwt;


import com.example.spartamatching_01.dto.SecurityExceptionDto;
import com.example.spartamatching_01.entity.UserRoleEnum;
import com.example.spartamatching_01.repository.LogoutAccessTokenRedisRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFiler extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);   //엑세스토큰은 AUTHORIZATION_HEADER 라는 키를 사용
        String refreshToken = request.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER); //리프레시토큰은 REFRESH_AUTHORIZATION_HEADER 라는 키를 사용

        //AccessToken 인증,인가 체크
        if (accessToken != null) {
            checkLogout(accessToken);
            if (accessToken.startsWith(JwtUtil.BEARER_PREFIX)) {    //일반키값으로 시작한다면
                String resolvedAccessToken = jwtUtil.resolveAccessToken(accessToken);
                if(validCheck(resolvedAccessToken, response)){
                    SetAuthentication(resolvedAccessToken, response);
                }
            }
        }
        //RefreshToken 인증,인가 체크
        if (refreshToken != null) {
            if (refreshToken.startsWith(JwtUtil.REFRESH_PREFIX)) {  //리프레시토큰 키값으로 시작한다면
                String resolvedFreshToken = jwtUtil.resolveRefreshToken(refreshToken);
                if(validCheck(resolvedFreshToken, response)){
                    SetAuthentication(resolvedFreshToken, response);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

        private void checkLogout(String accessToken) {
        if (logoutAccessTokenRedisRepository.existsById(resolveToken(accessToken))) {
            throw new IllegalArgumentException("이미 로그아웃된 회원입니다.");
        }
    }
    private String resolveToken(String token) {
        return token.substring(7);
    }


    public void setAuthentication(String username,String role) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(username,role);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }


    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public boolean validCheck(String Token, HttpServletResponse response){
        if (!jwtUtil.validateToken(Token)) {
            jwtExceptionHandler(response, "올바른 토큰이 아닙니다", HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;

    }

    public void SetAuthentication(String Token, HttpServletResponse response){
        Claims info = jwtUtil.getUserInfoFromToken(Token);


        //토큰이 들고있는 권한이 어드민이라면
        if(info.get(JwtUtil.AUTHORIZATION_KEY).equals("ADMIN")){
            setAuthentication(info.getSubject(),"ADMIN");
        }
        //토큰이 들고있는 권한이 유저라면
        if(info.get(JwtUtil.AUTHORIZATION_KEY).equals("USER")){
            setAuthentication(info.getSubject(),"USER");
        }
        //토큰이 권한을 들고있지않다면
        if (info.get(JwtUtil.AUTHORIZATION_KEY) == null) {
            throw new IllegalStateException("잘못된 권한정보입니다");
        }
    }





}
