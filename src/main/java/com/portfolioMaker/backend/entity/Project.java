package com.portfolioMaker.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



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