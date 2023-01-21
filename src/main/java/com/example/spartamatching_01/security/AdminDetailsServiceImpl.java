package com.example.spartamatching_01.security;

import com.example.spartamatching_01.entity.Admin;
import com.example.spartamatching_01.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDetailsServiceImpl implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("사용자를 찾을수 없습니다.")
        );
        return new AdminDetailsImpl(admin, admin.getUsername());
    }
}
