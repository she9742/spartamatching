package com.example.spartamatching_01.service;

import com.example.spartamatching_01.dto.client.*;
import com.example.spartamatching_01.dto.common.AllProductResponseDto;
import com.example.spartamatching_01.dto.common.PageDto;
import com.example.spartamatching_01.dto.common.ReissueResponseDto;
import com.example.spartamatching_01.dto.common.SignoutRequestDto;
import com.example.spartamatching_01.entity.*;
import com.example.spartamatching_01.jwt.JwtUtil;
import com.example.spartamatching_01.redis.CacheKey;
import com.example.spartamatching_01.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.example.spartamatching_01.entity.UserRoleEnum.USER;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final TalkRepository talkRepository;
    private final MessageRepository messageRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final MatchingRepository matchingRepository;
    private final TradeRepository tradeRepository;
    private final ApplicantRepository applicantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final SignoutAccessTokenRedisRepository signoutAccessTokenRedisRepository;

    @Transactional
    public String signup(SignupRequestDto signupRequestDto) {

        //비밀번호 인코드
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        //회원 중복 확인
        Optional<Client> clients = clientRepository.findByUsername(signupRequestDto.getUsername());
        if (clients.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        }

        Client client = new Client(signupRequestDto,password);
         clientRepository.save(client);


        return "회원가입 완료";
    }


    @Transactional
    public SigninResponseDto signin(SigninRequestDto signinRequestDto, HttpServletResponse response){

        // 사용자 확인
        Client client = clientRepository.findByUsername(signinRequestDto.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(signinRequestDto.getPassword(), client.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다");
        }

        String accessToken = jwtUtil.createToken(client.getUsername(), client.getRole());
        RefreshToken refreshToken = saveRefreshToken(client.getUsername());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(client.getUsername(),client.getRole()));

        return new SigninResponseDto(accessToken,refreshToken.getRefreshToken());
    }

    private RefreshToken saveRefreshToken(String username) {
        return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(username, jwtUtil.refreshToken(username,USER),jwtUtil.getRefreshTokenTime()));
    }


    @CacheEvict(value = CacheKey.USER, key = "#username")
    public String signout(SignoutRequestDto signoutRequestDto, String username) {
        String accessToken = resolveToken(signoutRequestDto.getAccessToken());
        long remainMilliSeconds = jwtUtil.getRemainMilliSeconds(accessToken);
        refreshTokenRedisRepository.deleteById(username);
        signoutAccessTokenRedisRepository.save(SignoutAccessToken.of(accessToken, username, remainMilliSeconds));
        return "로그아웃 완료";
    }

    private String resolveToken(String token) {
        return token.substring(7);
    }

    @Transactional
    public List<MessageResponseDto> getMessages(Long talkId, Client client) {
        Talk talk = talkRepository.findById(talkId).orElseThrow(
                () -> new NullPointerException("해당 톡방이 존재하지 않습니다.")
        );
        Product product = productRepository.findById(talk.getProductId()).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        if ((!talk.getClientId().equals(client.getId())) && (!product.getSellerId().equals(client.getId()))) {
            throw new IllegalArgumentException("해당 톡방에 접근권한이 없습니다.");
        }

        List<Message> messages = messageRepository.findAllByTalk(talkId);
        List<MessageResponseDto> messageResponseDtos = new ArrayList<>();
        for (Message message : messages) {
            messageResponseDtos.add(new MessageResponseDto(message));
        }
        return messageResponseDtos;
    }

    @Transactional
    public List<MessageResponseDto> sendMessages(Long talkId, Client client, MessageRequestDto messageRequestDto) {

        //톡방 존재여부 확인
        Talk talk = talkRepository.findById(talkId).orElseThrow(
                () -> new NullPointerException("톡방이 존재하지 않습니다.")
        );
        Product product = productRepository.findById(talk.getProductId()).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        if ((!talk.getClientId().equals(client.getId())) && (!product.getSellerId().equals(client.getId()))) {
            throw new IllegalArgumentException("해당 톡방에 접근권한이 없습니다.");
        }
        //톡방 활성화되있다면 메세지 전송 아니면 전송X
        if (talk.isActivation()) {
            Message message = new Message(talkId, client.getUsername(), messageRequestDto.getContent());
            messageRepository.save(message);
            List<Message> messages = messageRepository.findAllByTalk(talkId);
            List<MessageResponseDto> messageResponseDtos = new ArrayList<>();
            for (Message message1 : messages) {
                messageResponseDtos.add(new MessageResponseDto(message1));
            }
            return messageResponseDtos;

        } else {
            throw new IllegalArgumentException("종료된 톡방에는 메시지를 보낼 수 없습니다.");
        }
    }

    //프로필 만들기
    @Transactional
    public ProfileUpdateResponseDto updateProfile(ProfileUpdateRequestDto requestDto, Client client) {
        client.updateClientProfile(requestDto.getNickname(), requestDto.getImage());
        clientRepository.save(client);
        return new ProfileUpdateResponseDto(client);
    }

    // 프로필 가져오기
    @Transactional
    public ProfileUpdateResponseDto getProfile(Client client) {
        return new ProfileUpdateResponseDto(client);
    }

    // 전체 판매상품 목록 조회
    @Transactional(readOnly = true)
    public Page<AllProductResponseDto> getAllProducts(PageDto pageDto) {
        Pageable pageable = makePage(pageDto);
        Page<Product> AllProducts = productRepository.findAllByActivation(pageable,true);
        Page<AllProductResponseDto> allProductsResponse = AllProducts.map(AllProductResponseDto::new);
        return allProductsResponse;
    }


    // 전체 판매자 목록 조회
    @Transactional(readOnly = true)
    public Page<AllSellerResponseDto> getAllSellers (PageDto pageDto){
        Pageable pageable = makePage(pageDto);
        Page<Client> sellerList = clientRepository.findAllByIsSeller(pageable,true);
        Page<AllSellerResponseDto> sellerResponseList = sellerList.map(AllSellerResponseDto::new);
        return sellerResponseList;
    }

    // 판매자 정보 조회
    @Transactional
    public SellerResponseDto getSellerInfo (Long sellerId){
        Client seller = clientRepository.findById(sellerId).orElseThrow(
                () -> new RuntimeException("찾으시는 판매자가 없습니다.")
        );
        return new SellerResponseDto(seller);
    }

    @Transactional
    public String sendMatching (Long clientId, Long productId){

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NullPointerException("해당 상품이 존재하지 않습니다.")
        );
        Client seller = clientRepository.findById(product.getSellerId()).orElseThrow(
                () -> new IllegalArgumentException("해당 판매자가 존재하지 않습니다.")
        );
        matchingRepository.save(new Matching(clientId, productId,product.getSellerId()));
        return "매칭 요청에 성공했습니다.";
    }


    @Transactional
    public String buyProduct (Client client, Long productId){
        //가격입력을 위해 제품정보 로드
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );

        //해당 셀러와 사용자가 실제 거래중인지 확인
        Talk talk = talkRepository.findByClientIdAndProductId(client.getId(), product.getSellerId()).orElseThrow(
                () -> new IllegalArgumentException("해당 판매자와 거래중이 아닙니다")
        );

        if (!talk.isActivation()) {
            new IllegalArgumentException("비활성화된 거래입니다.");
        }

        //포인트 비교
        if (client.getPoint() >= product.getPoint()) {
            tradeRepository.save(new Trade(client.getId(), product.getSellerId(), productId));
        } else throw new IllegalArgumentException("잔액이 부족합니다.");

        return "물건을 구매하였습니다";

    }


    public Pageable makePage(PageDto pageDto){
        Sort.Direction direction = pageDto.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, pageDto.getSortBy());
        return PageRequest.of(pageDto.getPage() - 1, pageDto.getSize(), sort);
    }

    @Transactional
    public String applySeller(Client client,ApplySellerRequestDto applySellerRequestDto){
        //현재 판매자 등록 요청이 있는지 확인한다
        Optional<Applicant> sellerReq_ck = applicantRepository.findByClientId(client.getId());
        if(sellerReq_ck.isPresent()){
            //있다면 오류메시지를 띄우고 취소시킨다
            throw new IllegalArgumentException("이미 신청한 유저입니다.");
        }
        //이미 판매자인지 확인한다
        if(client.isSeller()){
            throw new IllegalArgumentException("이미 판매자입니다.");
        }

        //없다면 DB에 등록 요청을 등록한다
        Applicant applicant = new Applicant(client.getId(),applySellerRequestDto);
        applicantRepository.save(applicant);

        return "판매자 신청을 하였습니다.";
    }

    public Client findByUsername(String name) {
        return clientRepository.findByUsername(name).orElseThrow();
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
            String accessToken = jwtUtil.createToken(username, USER);
            return new ReissueResponseDto(accessToken,saveRefreshToken(username).getRefreshToken());
        }
        //전달받은 리프래쉬토큰이 DB에 저장된 리프레시 토큰과 같으며 + 리프레시토큰의 남은시간이 리프레시토큰의 총 경과시간을 지나지 않았으면 리프레시토큰 그대로 재사용
        return new ReissueResponseDto(jwtUtil.createToken(username, USER),refreshToken);
    }

    private boolean lessThanReissueExpirationTimesLeft(String refreshToken) {
        return jwtUtil.getRemainMilliSeconds(refreshToken) < jwtUtil.getRefreshTokenTime();
    }
}
