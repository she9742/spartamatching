package com.example.spartamatching_01.security;

import com.example.spartamatching_01.repository.ClientRepository;
import com.example.spartamatching_01.entity.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientDetailsServiceImpl implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("고객이 존재하지 않습니다.")
        );
        return new ClientDetailsImpl(client, client.getUsername());
    }
}
