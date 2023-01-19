package com.example.service;

import com.example.dto.*;
import com.example.entity.*;
import com.example.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final TalkRepository talkRepository;
    private final MessageRepository messageRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final ClientReqRepository clientReqRepository;
    private final TradeReqRepository tradeReqRepository;
    private final SellerReqRepository sellerReqRepository;
    private final PasswordEncoder passwordEncoder;



    @Transactional
    public String signup(SignupRequestDto signupRequestDto){

        //비밀번호 인코드
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        //회원 중복 확인
        Optional<Client> clients = clientRepository.findByUsername(signupRequestDto.getUsername());
        if (clients.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        }

        Client client = new Client(signupRequestDto,password);
        clientRepository.save(client);

        return "가입 완료";
    }


    @Transactional
    public String signin(SigninRequestDto signinRequestDto){
        // 사용자 확인
        Client client = clientRepository.findByUsername(signinRequestDto.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다")
        );
        // 비밀번호 확인
        //스프링 시큐리티 내부기능사용 입력된 비밀번호와 저장된 비밀번호 비교
        if (!passwordEncoder.matches(signinRequestDto.getPassword(), client.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다");
        }


        //리프레쉬토큰,엑세스토큰 연동필요
//        String accessToken = jwtUtil.createToken(client.getUsername(), client.권한());
//        String refreshToken1 = jwtUtil.refreshToken(client.getUsername(), client.권한());
//        return new TokenResponseDto(accessToken, refreshToken1);

        return "로그인 완료";

    }






    @Transactional
    public ResponseEntity<List<MessageResponseDto>> getMessages(long talkId) {
        List<Message> messages = messageRepository.findAllByTalk(talkId);
        List<MessageResponseDto> messageResponseDtos = new ArrayList<>();
        for(Message message : messages) {
            messageResponseDtos.add(new MessageResponseDto(message));
        }
        return ResponseEntity.ok().body(messageResponseDtos) ;
    }

    @Transactional
    public MessageResponseDto sendMessages(Long talkId,String writer, MessageRequestDto messageRequestDto) {

        //톡방 존재여부 확인
        Talk talk = talkRepository.findById(talkId).orElseThrow(
                () -> new NullPointerException("톡방이 존재하지 않습니다.")
        );

        //톡방 활성화되있다면 메세지 전송 아니면 전송X
        if(talk.isActivation()) {
            Message message = new Message(talkId,writer, messageRequestDto.getContent());
            messageRepository.save(message);
            return new MessageResponseDto(message);
        } else {
            return new MessageResponseDto("종료된 톡방에는 메시지를 보낼수 없습니다.");
        }
    }

    //프로필 만들기
    @Transactional
    public ProfileUpdateResponseDto updateProfile(ProfileUpdateRequestDto requestDto, Client client){
        client.updateClientProfile(requestDto.getNickname(), requestDto.getImage());
        return new ProfileUpdateResponseDto(client);
    }

    // 프로필 가져오기
    @Transactional

    public ProfileUpdateResponseDto getProfile(Client client){
        return new ProfileUpdateResponseDto(client);
    }

    // 전체 판매상품 목록 조회
    @Transactional(readOnly = true)
    public List<AllProductResponseDto> getAllProducts() {

        //모든 상품을 allproducts에 넣는다
        List<Product> AllProducts = productRepository.findAll();

        //반환을위해 AllProductsResponse를 만든다
        List<AllProductResponseDto> AllProductsResponse = new ArrayList<>();

        //하나씩 넣는다
        for (Product product : AllProducts) {
            Client sellers = clientRepository.findById(product.getSellerId()).orElseThrow(
                    //실제로는 마주치지 않는 오류
                    () -> new NullPointerException()
            );
            AllProductsResponse.add(new AllProductResponseDto(product, sellers));
        }
        return AllProductsResponse;

    }
    // 전체 판매자 목록 조회
    @Transactional(readOnly = true)
    public List<AllSellerResponseDto> getAllSellers(Pageable pageable){
        List<Client> sellerList = clientRepository.findAllBy(pageable);
        List<AllSellerResponseDto> sellerResponseList = new ArrayList<>();
        for (Client client: sellerList){
            //조건. 판매자인지 확인한다
            if (client.getisSeller()) {
                sellerResponseList.add(new AllSellerResponseDto(client));
            }
        }
        return sellerResponseList;
    }
    // 판매자 정보 조회
    @Transactional
    public SellerResponseDto getSellerInfo(Long sellerId){
        Client seller = clientRepository.findById(sellerId).orElseThrow(
                ()-> new RuntimeException("찾으시는 판매자가 없습니다.")
        );
        return new SellerResponseDto(seller);
    }

    @Transactional
    public String sendMatching(Long clientId,Long sellerId){
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        );
        Client seller = clientRepository.findById(sellerId).orElseThrow(
                () -> new IllegalArgumentException("해당 판매자가 존재하지 않습니다.")
        );
        clientReqRepository.save(new ClientReq(clientId,sellerId));
        return "매칭 요청에 성공했습니다.";
    }


    @Transactional
    public String buyProduct(Long clientId, Long productId){
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );

        //포인트 비교
        if (client.getPoint() >= product.getPoint()){
            tradeReqRepository.save(new TradeReq(clientId,productId));
        } else throw new IllegalArgumentException("잔액이 부족합니다.");

        return "물건을 구매하였습니다";

    }

    @Transactional
    public ResponseEntity<String> applySeller(Client client){


        //현재 판매자 등록 요청이 있는지 확인한다
        Optional<SellerReq> sellerReq_ck = sellerReqRepository.findByClientId(client.getId());
        if(sellerReq_ck.isPresent()){
            //있다면 오류메시지를 띄우고 취소시킨다
            throw new IllegalArgumentException("이미 신청한 유저입니다.");
        }
        //이미 판매자인지 확인한다
        if(client.getisSeller()){
            throw new IllegalArgumentException("이미 판매자입니다.");
        }
        //2.컨트롤러에서 userDetails를 사용하여 확인후 메소드 진입
        //->컨트롤러 생성후 이방식으로 변경




        //없다면 DB에 등록 요청을 등록한다
        SellerReq sellerReq = new SellerReq(client.getId());
        sellerReqRepository.save(sellerReq);

        return new ResponseEntity<>("판매자 신청을 하였습니다.", HttpStatus.OK);
    }

//    @Transactional
//    public void withdraw(Long clientId, Long productId){
//        Client client = clientRepository.findById(clientId).orElseThrow(
//                () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
//        );
//        Product product = productRepository.findById(productId).orElseThrow(
//                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
//        );
//
//    }

}
