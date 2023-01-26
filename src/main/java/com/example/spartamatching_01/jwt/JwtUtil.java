package com.example.spartamatching_01.jwt;

import com.example.spartamatching_01.entity.UserRoleEnum;
import com.example.spartamatching_01.security.AdminDetailsServiceImpl;
import com.example.spartamatching_01.security.ClientDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final ClientDetailsServiceImpl clientDetailsService;
    private final AdminDetailsServiceImpl adminDetailsService;



    //토큰 생성에 필요한 값
    public static final String AUTHORIZATION_HEADER = "Authorization"; //Header KEY 값
    public static final String REFRESH_AUTHORIZATION_HEADER = "Refresh_authorization"; //Header KEY 값
    public static final String AUTHORIZATION_KEY = "auth";  // 사용자 권한 값의 KEY.
    public static final String BEARER_PREFIX = "Bearer "; //토큰 식별자.
    public static final String REFRESH_PREFIX = "Refres "; //토큰 식별자.
    private static final long ACCESS_TOKEN_TIME = 30 * 1000L; //1 hour // 60min X 60sec X 1000ms
    private static final Long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L; // 14 day
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct //객체 생성 시 초기화
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }
    public static Long getRefreshTokenTime() {
        return REFRESH_TOKEN_TIME;
    }

    public String resolveAccessToken(String bearerToken) {
        //헤더가 null이 아니고 &&  헤더가 해당 Bearer 식별자로 시작한다면 식별자 제거
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
    public String resolveRefreshToken(String bearerToken) {
        //헤더가 null이 아니고 &&  헤더가 해당 Refres 식별자로 시작한다면 식별자 제거
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(REFRESH_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // 리프레시 토큰 생성
    public String refreshToken(String username, UserRoleEnum role) {
        Date date = new Date();
        return REFRESH_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty");
        }
        return false;
    }
    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch(ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String username,String role) {
        if(role.equals("ADMIN")){
            UserDetails adminDetails = adminDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(adminDetails, null, adminDetails.getAuthorities());
        }else{//role==USER
            UserDetails clientDetails = clientDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(clientDetails, null, clientDetails.getAuthorities());
        }

    }

    //리프레시토큰으로 재발급시 사용
    public Authentication getAuthentication(String token) {
        // Jwt 에서 claims 추출
        Claims claims = getUserInfoFromToken(token);

        //토큰이 권한을 들고있지않다면
        if (claims.get(AUTHORIZATION_KEY) == null) {
            throw new IllegalStateException("잘못된 권한정보입니다");
        }
        //토큰이 들고있는 권한이 어드민이라면
        if(claims.get(AUTHORIZATION_KEY).equals("ADMIN")){
            UserDetails adminDetails = adminDetailsService.loadUserByUsername(claims.getSubject());
            //유저정보 + 크리티컬한정보 저장 + 유저의권한
            return new UsernamePasswordAuthenticationToken(adminDetails, null, adminDetails.getAuthorities());
        }
        //토큰이 들고있는 권한이 유저라면
        else{ //claims.get(AUTHORIZATION_KEY)== user)
            UserDetails clientDetails = clientDetailsService.loadUserByUsername(claims.getSubject());
            //유저정보 + 크리티컬한정보 저장 + 유저의권한
            return new UsernamePasswordAuthenticationToken(clientDetails, null, clientDetails.getAuthorities());
        }


    }
    //남은 시간 계산
    public long getRemainMilliSeconds(String token) {
        Claims info = getUserInfoFromToken(token);
        Date expiration = info.getExpiration();
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }






}
