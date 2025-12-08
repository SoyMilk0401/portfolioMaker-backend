package com.portfolioMaker.backend.service;

import com.portfolioMaker.backend.dto.ai.GeminiRequest;
import com.portfolioMaker.backend.dto.ai.GeminiResponse;
import com.portfolioMaker.backend.dto.response.PortfolioDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.url}")
    private String apiUrl;

    private final RestClient restClient = RestClient.create();

    public String getPortfolioAdvice(PortfolioDetailResponse portfolio) {
        String prompt = createPrompt(portfolio);

        GeminiRequest request = new GeminiRequest(
                Collections.singletonList(new GeminiRequest.Content(
                        Collections.singletonList(new GeminiRequest.Part(prompt))
                ))
        );

        GeminiResponse response = restClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .header("Content-Type", "application/json")
                .body(request)
                .retrieve()
                .body(GeminiResponse.class);

        if (response != null && !response.getCandidates().isEmpty()) {
            return response.getCandidates().get(0).getContent().getParts().get(0).getText();
        }
        return "AI 조언을 가져오는 데 실패했습니다.";
    }

    private String createPrompt(PortfolioDetailResponse p) {
        StringBuilder sb = new StringBuilder();
        sb.append("당신은 취업 전문 커리어 코치입니다. 아래 개발자 포트폴리오를 분석하고 개선점을 구체적으로 조언해주세요.\n\n");
        sb.append("--- 포트폴리오 정보 ---\n");
        sb.append("제목: ").append(p.getDescription().getTitle()).append("\n");
        sb.append("자기소개: ").append(p.getDescription().getDetail()).append("\n");
        sb.append("기술 스택: ").append(p.getTechStack()).append("\n");

        sb.append("--- 프로젝트 목록 ---\n");
        for (var project : p.getProjects()) {
            sb.append("- 프로젝트명: ").append(project.getTitle()).append("\n");
            sb.append("  설명: ").append(project.getDescription()).append("\n");
            sb.append("  기간: ").append(project.getDate()).append("\n");
        }

        sb.append("\n--- 요청 사항 ---\n");
        sb.append("1. 자기소개서의 강점과 약점을 분석해주세요.\n");
        sb.append("2. 기술 스택과 프로젝트 경험이 잘 매칭되는지 평가해주세요.\n");
        sb.append("3. 채용 담당자 입장에서 보완했으면 하는 점을 3가지 꼽아주세요.\n");
        sb.append("응답은 텍스트 형식과 마크다운 줄바꿈으로 깔끔하게 정리해주세요.");
        sb.append("이모티콘 사용은 하지 마세요.");

        return sb.toString();
    }
}