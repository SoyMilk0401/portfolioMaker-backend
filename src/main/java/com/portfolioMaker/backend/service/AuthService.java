package com.portfolioMaker.backend.service;

import com.portfolioMaker.backend.dto.request.LoginRequest;
import com.portfolioMaker.backend.dto.request.SignupRequest;
import com.portfolioMaker.backend.entity.Member;
import com.portfolioMaker.backend.repository.MemberRepository;
import com.portfolioMaker.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(SignupRequest request) {
        if (memberRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .build();

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public String login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 아이디입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(member.getUsername());
    }

    @Transactional
    public void leave(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        memberRepository.delete(member);
    }
}