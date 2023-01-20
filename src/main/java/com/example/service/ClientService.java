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
    public ResponseEntity<List<MessageResponseDto>> getMessages(Long talkId, Client client) {
        // 1. talk.getClientId와 clientId 가 일치하는지 확인 해야함
        // 2. 불일치한다면, 불일치 메세지를 날리고, 일치하면 메소드를 실행시킴.
        Talk talk = talkRepository.findById(talkId).orElseThrow(
                () -> new NullPointerException("해당 톡방이 존재하지 않습니다.")
        );
        if ((!talk.getClientId().equals(client.getId()))  ||  (!talk.getSellerId().equals(client.getId()))){
            throw new IllegalArgumentException("해당 톡방에 접근권한이 없습니다.");
        }

        List<Message> messages = messageRepository.findAllByTalk(talkId);
        List<MessageResponseDto> messageResponseDtos = new ArrayList<>();
        for(Message message : messages) {
            messageResponseDtos.add(new MessageResponseDto(message));
        }
        return ResponseEntity.ok().body(messageResponseDtos) ;
    }

    @Transactional
    public MessageResponseDto sendMessages(Long talkId, Client client, MessageRequestDto messageRequestDto) {

        //톡방 존재여부 확인
        Talk talk = talkRepository.findById(talkId).orElseThrow(
                () -> new NullPointerException("톡방이 존재하지 않습니다.")
        );
        if ((!talk.getClientId().equals(client.getId()))  ||  (!talk.getSellerId().equals(client.getId()))){
            throw new IllegalArgumentException("해당 톡방에 접근권한이 없습니다.");
        }
        //톡방 활성화되있다면 메세지 전송 아니면 전송X
        if(talk.isActivation()) {
            Message message = new Message(talkId,client.getUsername(), messageRequestDto.getContent());
            messageRepository.save(message);
            return new MessageResponseDto(message);
        } else {
            return new MessageResponseDto("종료된 톡방에는 메시지를 보낼수 없습니다.");
        }
    }

    //프로필 만들기
    @Transactional
    public ProfileUpdateResponseDto updateProfile(ProfileUpdateRequestDto requestDto, Client  client){
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
    public List<AllSellerResponseDto> getAllSellers(){
        List<Client> sellerList = clientRepository.findAll();
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

        //내가 보냈는데 내가 없을리가없음
//        Client client = clientRepository.findById(clientId).orElseThrow(
//                () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
//        );
        Client seller = clientRepository.findById(sellerId).orElseThrow(
                () -> new IllegalArgumentException("해당 판매자가 존재하지 않습니다.")
        );
        clientReqRepository.save(new ClientReq(clientId,sellerId));
        return "매칭 요청에 성공했습니다.";
    }


    @Transactional
    //public String buyProduct(Long clientId, Long productId){
    //public String buyProduct(Client client, Long productId){
    public String buyProduct(Client client, Long productId){
        //물건 번호만 가지고 물건을 살수있다?
            //->안됨. 연결된 판매자와의 물건만 살 수 있어야함
            //->연결된사람인지 검증수단필요
            //->Talk가 연결된 판매자만 검증됨



        //가격입력을 위해 제품정보 로드
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );

        //해당 셀러와 사용자가 실제 거래중인지 확인
        Talk talk = talkRepository.findByClientIdAndSellerId(client.getId(),product.getSellerId()).orElseThrow(
                () -> new IllegalArgumentException("해당 판매자와 거래중이 아닙니다")
        );

        if(!talk.isActivation()){
            new IllegalArgumentException("비활성화된 거래입니다.");
        }




        //클라이언트 정보를 통째로 받아오는걸로 수정하면 검색불필요
//        Client client = clientRepository.findById(clientId).orElseThrow(
//                () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
//        );

        //셀러아이디와 같이검색하므로 불필요
//        Product product = productRepository.findById(productId).orElseThrow(
//                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
//        );

        //포인트 비교





        if (client.getPoint() >= product.getPoint()){
            tradeReqRepository.save(new TradeReq(client.getId(),product.getSellerId(),productId));
        } else throw new IllegalArgumentException("잔액이 부족합니다.");

        return "물건을 구매하였습니다";

    }

    @Transactional
    public ResponseEntity<String> applySeller(Client client,ApplySellerRequestDto applySellerRequestDto){


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
        SellerReq sellerReq = new SellerReq(client.getId(),applySellerRequestDto);
        sellerReqRepository.save(sellerReq);

        return new ResponseEntity<>("판매자 신청을 하였습니다.", HttpStatus.OK);
    }


}
