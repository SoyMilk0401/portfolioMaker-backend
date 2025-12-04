package com.portfolioMaker.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;


    private String name;
    private String birthdate;
    private String email;
    private String phone;
    private String education;
    private String githubUsername;
    private String photoUrl; // 프론트의 photo


    @Column(columnDefinition = "TEXT")
    private String techStack;


    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RelatedLink> relatedLinks = new ArrayList<>();

    @Builder
    public Portfolio(Member member, String title, String description, String name, String techStack) {
        this.member = member;
        this.title = title;
        this.description = description;
        this.name = name;
        this.techStack = techStack;
    }
}