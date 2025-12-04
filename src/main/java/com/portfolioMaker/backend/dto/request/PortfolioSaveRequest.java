package com.portfolioMaker.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class PortfolioSaveRequest {

    private DescriptionDto description;
    private UserInfoDto userInfo;
    private TechStackDto techStack;
    private List<ProjectDto> projects;
    private List<LinkDto> relatedLinks;


    @Getter @NoArgsConstructor
    public static class DescriptionDto {
        private String title;
        private String detail;
    }

    @Getter @NoArgsConstructor
    public static class UserInfoDto {
        private String name;
        private String birthdate;
        private String email;
        private String phone;
        private String education;
        private String githubUsername;
        private String photo; // 이미지 URL
    }

    @Getter @NoArgsConstructor
    public static class TechStackDto {
        private List<String> language;
        private List<String> frontend;
        private List<String> backend;
        private List<String> devops;
    }

    @Getter @NoArgsConstructor
    public static class ProjectDto {
        private String title;
        private String date;
        private String description;
        private String url;
        private String video;
    }

    @Getter @NoArgsConstructor
    public static class LinkDto {
        private String name;
        private String url;
        private String description;
    }
}