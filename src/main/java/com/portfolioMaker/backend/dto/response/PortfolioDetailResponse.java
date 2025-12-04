package com.portfolioMaker.backend.dto.response;

import com.portfolioMaker.backend.entity.Portfolio;
import com.portfolioMaker.backend.entity.Project;
import com.portfolioMaker.backend.entity.RelatedLink;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PortfolioDetailResponse {

    private Long id;
    private DescriptionDto description;
    private UserInfoDto userInfo;
    private Object techStack;
    private List<ProjectDto> projects;
    private List<LinkDto> relatedLinks;

    public PortfolioDetailResponse(Portfolio entity, Object techStackObj) {
        this.id = entity.getId();
        this.techStack = techStackObj; // 서비스에서 변환해서 넘겨줌

        this.description = new DescriptionDto(entity.getTitle(), entity.getDescription());

        this.userInfo = new UserInfoDto(
                entity.getName(), entity.getBirthdate(), entity.getEmail(),
                entity.getPhone(), entity.getEducation(), entity.getGithubUsername(), entity.getPhotoUrl()
        );

        this.projects = entity.getProjects().stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList());

        this.relatedLinks = entity.getRelatedLinks().stream()
                .map(LinkDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class DescriptionDto {
        private String title;
        private String detail;
        public DescriptionDto(String title, String detail) { this.title = title; this.detail = detail; }
    }

    @Getter
    public static class UserInfoDto {
        private String name;
        private String birthdate;
        private String email;
        private String phone;
        private String education;
        private String githubUsername;
        private String photo;

        public UserInfoDto(String name, String birthdate, String email, String phone, String education, String githubUsername, String photo) {
            this.name = name; this.birthdate = birthdate; this.email = email; this.phone = phone; this.education = education; this.githubUsername = githubUsername; this.photo = photo;
        }
    }

    @Getter
    public static class ProjectDto {
        private String title;
        private String date;
        private String description;
        private String url;
        private String video;

        public ProjectDto(Project p) {
            this.title = p.getTitle();
            this.date = p.getDate();
            this.description = p.getDescription();
            this.url = p.getUrl();
            this.video = p.getVideo();
        }
    }

    @Getter
    public static class LinkDto {
        private String name;
        private String url;
        private String description;

        public LinkDto(RelatedLink l) {
            this.name = l.getName();
            this.url = l.getUrl();
            this.description = l.getDescription();
        }
    }
}