package com.portfolioMaker.backend.controller;

import com.portfolioMaker.backend.dto.request.PortfolioSaveRequest;
import com.portfolioMaker.backend.dto.response.PortfolioDetailResponse;
import com.portfolioMaker.backend.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping
    public ResponseEntity<Long> createPortfolio(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PortfolioSaveRequest requestDto) {

        Long savedId = portfolioService.createPortfolio(userDetails.getUsername(), requestDto);
        return ResponseEntity.ok(savedId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePortfolio(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PortfolioSaveRequest requestDto) {

        portfolioService.updatePortfolio(id, userDetails.getUsername(), requestDto);
        return ResponseEntity.ok("포트폴리오 수정 완료");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioDetailResponse> getPortfolio(@PathVariable Long id) {
        return ResponseEntity.ok(portfolioService.getPortfolio(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PortfolioDetailResponse>> getMyPortfolios(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(portfolioService.getMyPortfolios(userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<PortfolioDetailResponse>> getAllPortfolios() {
        return ResponseEntity.ok(portfolioService.getAllPortfolios());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePortfolio(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        portfolioService.deletePortfolio(id, userDetails.getUsername());
        return ResponseEntity.ok("포트폴리오 삭제 완료");
    }

}