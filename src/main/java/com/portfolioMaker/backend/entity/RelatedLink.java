package com.portfolioMaker.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RelatedLink {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    private String name;

    private String url;

    private String description;

    @Builder
    public RelatedLink(Portfolio portfolio, String name, String url, String description) {
        this.portfolio = portfolio;
        this.name = name;
        this.url = url;
        this.description = description;
    }
}