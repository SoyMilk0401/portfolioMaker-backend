package com.portfolioMaker.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 포트폴리오 연결 편의 메서드 (선택 사항)
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    private String title;

    private String date;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String url;

    private String video;

    @Builder
    public Project(Portfolio portfolio, String title, String date, String description, String url, String video) {
        this.portfolio = portfolio;
        this.title = title;
        this.date = date;
        this.description = description;
        this.url = url;
        this.video = video;
    }
}