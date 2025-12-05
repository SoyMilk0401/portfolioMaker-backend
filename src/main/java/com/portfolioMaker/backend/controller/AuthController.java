package com.portfolioMaker.backend.controller;

import com.portfolioMaker.backend.dto.request.LeaveRequest;
import com.portfolioMaker.backend.dto.request.LoginRequest;
import com.portfolioMaker.backend.dto.request.SignupRequest;
import com.portfolioMaker.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> leave(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody LeaveRequest request) {

        authService.leave(userDetails.getUsername(), request.getPassword());
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}