# GitHub Copilot Instructions

## 언어 및 응답 스타일
**항상 한국어로 응답해주세요.**

**응답은 간결하고 핵심만 포함하도록 작성하세요:**
- 불필요한 설명이나 반복 제거
- 핵심 정보와 코드만 제공
- 짧고 명확한 문장 사용
- 빠른 이해와 처리를 위해 간단명료하게 작성

## 프로젝트 개요
이 프로젝트는 일본 지역별 커뮤니티를 위한 Spring Boot 기반 백엔드 애플리케이션입니다.

### 기술 스택
- **Backend Framework**: Spring Boot 4.0.1
- **Language**: Java 17
- **Database**: MySQL (프로덕션), H2 (개발/테스트)
- **ORM**: Spring Data JPA
- **Build Tool**: Maven
- **Additional Libraries**:
  - Lombok (코드 간소화)
  - SpringDoc OpenAPI (API 문서화)
  - Thymeleaf (서버 사이드 HTML 렌더링 - 일부 페이지용)

## 프로젝트 구조
```
src/main/java/com/example/join/
├── JoinApplication.java          # 메인 애플리케이션 클래스
├── controller/                   # REST API 컨트롤러
├── service/                      # 비즈니스 로직
├── repository/                   # 데이터 접근 레이어
└── entity/                       # JPA 엔티티
```

### 주요 기능 모듈
- **User**: 사용자 관리
- **Profile**: 프로필 관리
- **Post**: 게시글 관리
- **Comment**: 댓글 관리
- **FoodBoard**: 음식 게시판
- **Like**: 좋아요 기능

## 코딩 가이드라인

### Java 코드 스타일
1. **Lombok 사용**: 
   - `@Data`, `@Getter`, `@Setter`, `@Builder` 등을 적극 활용하여 보일러플레이트 코드 최소화
   - `@Slf4j`를 사용하여 로깅 구현

2. **레이어 아키텍처 준수**:
   - Controller → Service → Repository 순서로 호출
   - 각 레이어의 책임을 명확히 분리

3. **네이밍 규칙**:
   - 클래스명: PascalCase (예: `UserController`, `PostService`)
   - 메서드명: camelCase (예: `getUserById`, `createPost`)
   - 상수: UPPER_SNAKE_CASE (예: `MAX_PAGE_SIZE`)

4. **어노테이션**:
   - Controller: `@RestController` (REST API) 또는 `@Controller` (HTML 뷰 반환)
   - Service: `@Service`
   - Repository: `@Repository` (Spring Data JPA 인터페이스는 자동으로 구현됨)
   - 트랜잭션: `@Transactional`

### API 설계
- RESTful 원칙 준수
- HTTP 메서드 적절히 사용 (GET, POST, PUT, DELETE)
- 응답 상태 코드 명확히 설정
- DTO를 사용하여 엔티티 직접 노출 방지

### 데이터베이스
- JPA 엔티티 관계 매핑 명확히 정의 (`@OneToMany`, `@ManyToOne` 등)
- 지연 로딩(`FetchType.LAZY`)을 기본으로 사용
- 필요시 쿼리 최적화 (N+1 문제 방지)

## 개발 워크플로우

### 빌드 및 실행
```bash
# 빌드
./mvnw clean install

# 애플리케이션 실행
./mvnw spring-boot:run

# 테스트 실행
./mvnw test
```

### 개발 시 주의사항
1. 코드 변경 전 기존 테스트가 통과하는지 확인
2. 새로운 기능 추가 시 적절한 테스트 코드 작성
3. API 변경 시 OpenAPI 문서가 자동으로 업데이트되는지 확인
4. 민감한 정보(DB 비밀번호 등)는 환경 변수나 설정 파일로 관리

## 보안
- 사용자 입력 검증 필수
- SQL 인젝션 방지 (JPA 사용으로 기본 보호)
- XSS 공격 방지
- 민감한 데이터는 암호화하여 저장

## 문서화
- 복잡한 비즈니스 로직에는 주석 추가
- Public API 메서드에는 JavaDoc 작성
- OpenAPI를 통해 REST API 자동 문서화

## Git 커밋 메시지
- 명확하고 간결하게 작성
- 형식: `[타입] 간단한 설명`
  - feat: 새로운 기능 추가
  - fix: 버그 수정
  - refactor: 코드 리팩토링
  - docs: 문서 수정
  - test: 테스트 코드 추가/수정

## Pull Request 리뷰
- **PR 리뷰는 커밋 메시지가 어떤 언어로 작성되었든 항상 한국어로 작성해야 합니다**
- GitHub Copilot은 PR에 대한 자동 코드 리뷰를 제공합니다
- Repository 설정에서 GitHub Copilot을 활성화하면 사용할 수 있습니다
- Copilot 리뷰는 다음 항목을 중점적으로 확인합니다:
  - 코드 품질 및 베스트 프랙티스 준수
  - 잠재적 버그나 보안 취약점
  - 성능 최적화 가능성
  - 코드 가독성 및 유지보수성
- 자동 리뷰 결과를 참고하여 코드를 개선하세요
- PR 코멘트에서 Copilot에게 추가 질문을 할 수 있습니다 (@github-copilot 멘션)
