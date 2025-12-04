package com.portfolioMaker.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolioMaker.backend.dto.request.PortfolioSaveRequest;
import com.portfolioMaker.backend.dto.response.PortfolioDetailResponse;
import com.portfolioMaker.backend.entity.*;
import com.portfolioMaker.backend.repository.MemberRepository;
import com.portfolioMaker.backend.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Long createPortfolio(String username, PortfolioSaveRequest dto) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        String techStackJson = convertTechStackToJson(dto.getTechStack());

        Portfolio portfolio = Portfolio.builder()
                .member(member)
                .title(dto.getDescription().getTitle())
                .description(dto.getDescription().getDetail())
                .name(dto.getUserInfo().getName())
                .techStack(techStackJson)
                .build();

        mapUserInfo(portfolio, dto.getUserInfo());

        addProjects(portfolio, dto);
        addLinks(portfolio, dto);

        portfolioRepository.save(portfolio);
        return portfolio.getId();
    }

    @Transactional
    public void updatePortfolio(Long portfolioId, String username, PortfolioSaveRequest dto) {

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("포트폴리오가 없습니다."));

        if (!portfolio.getMember().getUsername().equals(username)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        String techStackJson = convertTechStackToJson(dto.getTechStack());

        portfolio.setTitle(dto.getDescription().getTitle());
        portfolio.setDescription(dto.getDescription().getDetail());
        portfolio.setTechStack(techStackJson);
        mapUserInfo(portfolio, dto.getUserInfo());

        portfolio.getProjects().clear();
        addProjects(portfolio, dto);

        portfolio.getRelatedLinks().clear();
        addLinks(portfolio, dto);
    }

    @Transactional(readOnly = true)
    public PortfolioDetailResponse getPortfolio(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("포트폴리오가 없습니다."));

        // JSON String -> Object 변환
        Object techStackObj = null;
        try {
            if (portfolio.getTechStack() != null && !portfolio.getTechStack().isEmpty()) {
                techStackObj = objectMapper.readValue(portfolio.getTechStack(), Object.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("TechStack 파싱 오류", e);
        }

        return new PortfolioDetailResponse(portfolio, techStackObj);
    }

    @Transactional(readOnly = true)
    public List<PortfolioDetailResponse> getAllPortfolios() {
        return portfolioRepository.findAll().stream()
                .map(portfolio -> {
                    Object techStackObj = null;
                    try {
                        if (portfolio.getTechStack() != null) {
                            techStackObj = objectMapper.readValue(portfolio.getTechStack(), Object.class);
                        }
                    } catch (Exception e) { /* 무시 */ }
                    return new PortfolioDetailResponse(portfolio, techStackObj);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PortfolioDetailResponse> getMyPortfolios(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return member.getPortfolios().stream() // Member에 @OneToMany가 되어있어야 함
                .map(portfolio -> {
                    // 리스트 조회에서도 JSON 변환 필요
                    Object techStackObj = null;
                    try {
                        if (portfolio.getTechStack() != null) {
                            techStackObj = objectMapper.readValue(portfolio.getTechStack(), Object.class);
                        }
                    } catch (Exception e) { /* 무시하거나 로그 */ }
                    return new PortfolioDetailResponse(portfolio, techStackObj);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePortfolio(Long id, String username) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("포트폴리오가 없습니다."));

        if (!portfolio.getMember().getUsername().equals(username)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        portfolioRepository.delete(portfolio);
    }

    private String convertTechStackToJson(PortfolioSaveRequest.TechStackDto techStack) {
        try {
            return objectMapper.writeValueAsString(techStack);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void addProjects(Portfolio portfolio, PortfolioSaveRequest dto) {
        if (dto.getProjects() != null) {
            for (PortfolioSaveRequest.ProjectDto pDto : dto.getProjects()) {
                Project project = Project.builder()
                        .portfolio(portfolio)
                        .title(pDto.getTitle())
                        .date(pDto.getDate())
                        .description(pDto.getDescription())
                        .url(pDto.getUrl())
                        .video(pDto.getVideo())
                        .build();
                portfolio.getProjects().add(project);
            }
        }
    }

    private void addLinks(Portfolio portfolio, PortfolioSaveRequest dto) {
        if (dto.getRelatedLinks() != null) {
            for (PortfolioSaveRequest.LinkDto lDto : dto.getRelatedLinks()) {
                RelatedLink link = RelatedLink.builder()
                        .portfolio(portfolio)
                        .name(lDto.getName())
                        .url(lDto.getUrl())
                        .description(lDto.getDescription())
                        .build();
                portfolio.getRelatedLinks().add(link);
            }
        }
    }

    private void mapUserInfo(Portfolio p, PortfolioSaveRequest.UserInfoDto info) {
        p.setName(info.getName());
        p.setBirthdate(info.getBirthdate());
        p.setEmail(info.getEmail());
        p.setPhone(info.getPhone());
        p.setEducation(info.getEducation());
        p.setGithubUsername(info.getGithubUsername());
        p.setPhotoUrl(info.getPhoto());
    }
}