package com.portfolioMaker.backend.controller;

import com.portfolioMaker.backend.dto.response.PortfolioDetailResponse;
import com.portfolioMaker.backend.service.GeminiService;
import com.portfolioMaker.backend.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final PortfolioService portfolioService;
    private final GeminiService geminiService;

    @PostMapping("/advice/{portfolioId}")
    public ResponseEntity<?> getPortfolioAdvice(@PathVariable Long portfolioId) {
        try {
            PortfolioDetailResponse portfolio = portfolioService.getPortfolio(portfolioId);

            String advice = geminiService.getPortfolioAdvice(portfolio);

            return ResponseEntity.ok(Map.of("advice", advice));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "AI 분석 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
}