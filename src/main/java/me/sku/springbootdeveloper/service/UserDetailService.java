package me.sku.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.sku.springbootdeveloper.domain.User;
import me.sku.springbootdeveloper.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {//사용자 정보를 가져오는 인터페이스 구현

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) {//필수
        return userRepository.findByEmail(email)
                 .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}
