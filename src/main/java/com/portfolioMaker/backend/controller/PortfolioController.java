package com.portfolioMaker.backend.controller;

import com.portfolioMaker.backend.dto.request.PortfolioSaveRequest;
import com.portfolioMaker.backend.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping
    public ResponseEntity<?> createPortfolio(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PortfolioSaveRequest requestDto) {
        try {
            Long savedId = portfolioService.createPortfolio(userDetails.getUsername(), requestDto);
            return ResponseEntity.ok(savedId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePortfolio(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PortfolioSaveRequest requestDto) {
        try {
            portfolioService.updatePortfolio(id, userDetails.getUsername(), requestDto);
            return ResponseEntity.ok("포트폴리오 수정 완료");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPortfolio(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(portfolioService.getPortfolio(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyPortfolios(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다."));
        }
        try {
            return ResponseEntity.ok(portfolioService.getMyPortfolios(userDetails.getUsername()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPortfolios() {
        try {
            return ResponseEntity.ok(portfolioService.getAllPortfolios());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePortfolio(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            portfolioService.deletePortfolio(id, userDetails.getUsername());
            return ResponseEntity.ok("포트폴리오 삭제 완료");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}