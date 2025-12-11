#  PortfolioMaker Backend

개발자 포트폴리오를 쉽고 빠르게 생성하고, Google Gemini AI를 통해 포트폴리오에 대한 구체적인 피드백과 조언을 받을 수 있는 REST API 서버입니다.

![poster](https://github.com/SoyMilk0401/portfolioMaker-backend/blob/main/Architecture%20Diagram.png?raw=true)

## 주요 기능

### 1. 사용자 인증 (Authentication)
- JWT(Json Web Token) 기반의 인증 시스템
- 회원가입 (`/api/auth/signup`), 로그인 (`/api/auth/login`), 회원탈퇴 기능 제공
- BCrypt를 이용한 비밀번호 암호화

### 2. 포트폴리오 관리 (Portfolio Management)
- 포트폴리오 생성, 조회, 수정, 삭제 (CRUD)
- 기술 스택(Tech Stack)을 JSON 형태로 유연하게 저장
- 프로젝트 경험 및 관련 링크(Github, Blog 등) 관리

### 3. AI 포트폴리오 조언 (AI Advisor)
- Google Gemini 모델 연동
- 작성된 포트폴리오(자기소개, 기술 스택, 프로젝트)를 분석
- 강점/약점 분석 및 채용 담당자 관점의 보완점 3가지 피드백 제공

##  API Reference

| Tag | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **Auth** | POST | `/api/auth/signup` | 회원가입 |
| | POST | `/api/auth/login` | 로그인 (JWT 발급) |
| | DELETE | `/api/auth/me` | 회원 탈퇴 (Auth Required) |
| **Portfolio** | GET | `/api/portfolios` | 전체 포트폴리오 조회 |
| | POST | `/api/portfolios` | 포트폴리오 생성 (Auth Required) |
| | GET | `/api/portfolios/my` | 내 포트폴리오 조회 (Auth Required) |
| | GET | `/api/portfolios/{id}` | 특정 포트폴리오 상세 조회 |
| | PUT | `/api/portfolios/{id}` | 포트폴리오 수정 (Auth Required) |
| | DELETE | `/api/portfolios/{id}` | 포트폴리오 삭제 (Auth Required) |
| **AI** | POST | `/api/ai/advice/{id}` | 포트폴리오 AI 분석 요청 (Auth Required) |

##  Environment Variables

프로젝트 실행을 위해 프로젝트 루트에 `.env` 파일을 생성하거나 환경 변수를 설정해야 합니다.

```properties
JWT_SECRET=your_secret_key_should_be_long_enough
GEMINI_API_KEY=your_gemini_api_key
DB_URL=jdbc:mysql://localhost:3306/your_db
DB_USERNAME=root
DB_PASSWORD=password
```

## Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.5.8
- **Database**: H2 (Local), MySQL (Prod)
- **ORM**: Spring Data JPA
- **Security**: Spring Security, JWT (JJWT 0.11.5)
- **AI Integration**: Google Gemini API (Generative AI)
- **Build Tool**: Gradle
