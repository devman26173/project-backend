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

## Merge 메시지 형식
- PR merge 시 다음 형식을 사용하세요: `Merge PR/{owner}/{branch_name} (#{pr_number})`
- 예시: `Merge PR/devman26173/feature/button_of_home_page (#128)`
- 자세한 가이드: [MERGE_MESSAGE_GUIDE.md](.github/MERGE_MESSAGE_GUIDE.md)
- 자동 merge: PR에 `auto-merge` 라벨 추가

## Pull Request 리뷰
- **PR 리뷰는 커밋 메시지가 어떤 언어로 작성되었든 항상 한국어로 작성해야 합니다**
- GitHub Copilot은 PR에 대한 자동 코드 리뷰를 제공합니다
- Repository 설정에서 GitHub Copilot을 활성화하면 사용할 수 있습니다
- Copilot 리뷰는 다음 항목을 중점적으로 확인합니다:
  - 코드 품질 및 베스트 프랙티스 준수
  - 잠재적 버그
  - 성능 최적화 가능성
  - 코드 가독성 및 유지보수성
  - 코드 오너십 검증
- 자동 리뷰 결과를 참고하여 코드를 개선하세요
- PR 코멘트에서 Copilot에게 추가 질문을 할 수 있습니다 (@github-copilot 멘션)

### 코드 오너십 검증
PR 리뷰 시 다음 사항을 확인하고 표시해야 합니다:
- 수정된 파일이 특정 contributor의 담당 영역인지 확인
- 담당자가 아닌 사람이 담당 영역을 수정한 경우, **"⚠️ [파일명]: 이 파일은 [담당자]의 담당 영역입니다."** 형식으로 명시
- 담당 영역은 아래 "Contributor 담당 파트" 섹션의 **"담당 영역"** 참조
- **주의**: "커밋한 파일" 목록은 Git 히스토리 기반이며, 현재 담당 영역과 다를 수 있습니다. 오너십 검증은 **"담당 영역"**을 기준으로 합니다.
- 공통 파일(JoinApplication.java, application.properties 등)이나 여러 contributor가 관여한 파일은 검증 대상에서 제외합니다.

## Contributor 담당 파트
프로젝트의 주요 기여자와 각자의 담당 영역입니다.

**참고**: 파일 목록은 Git 커밋 히스토리를 기반으로 작성되었습니다. 일부 파일은 여러 contributor가 수정했을 수 있으며, 같은 파일이 여러 contributor 목록에 나타날 수 있습니다. 또한 일부 파일은 개발 과정에서 이름이 변경되거나 삭제되었을 수 있습니다.

### devman26173
**역할**: 프로젝트 메인 개발자 및 전체 아키텍처 담당
- **Backend 개발**:
  - Spring Boot 애플리케이션 초기 설정 및 구성
- **인프라 및 설정**:
  - Docker 설정 (Dockerfile, docker-compose.yml)
  - Maven 빌드 구성 (pom.xml)
  - 애플리케이션 설정 (application.properties, application-prod.yml)
  - 프로젝트 문서화 (Readme.md)

### devhyunju
**역할**: FoodBoard 시스템 개발
- **담당 영역**:
  - FoodBoard 관련 모든 파일 (Controller, Entity, Repository, Service)
  - 파일 업로드 기능 (FileUploadController)
  - FoodBoard 관련 템플릿 4개 (foodboard.html, foodboard-edit.html, foodboard-view.html, foodboard-write.html)
  - FoodBoard CSS (foodboard.css)

### goodsujin
**역할**: 사용자 인증 및 회원가입 시스템 개발
- **담당 영역**:
  - 사용자 인증 시스템 (Login, Signup)
  - User/SignupForm 관련 전체 레이어 (Controller, Entity, Service, Repository)
  - 로그인/회원가입 관련 템플릿 5개 (user-login.html, user-signup.html, login.html, signupform.html, logout.html)
  - 로그인/회원가입 CSS (login.css, signupform.css)

### java0731kk
**역할**: Post 게시판 및 댓글/좋아요 기능 개발
- **담당 영역**:
  - Post 게시판 시스템 (PostController)
  - Comment, Like 기능 (Entity, Repository, Service)
  - Post 템플릿 (post.html)
  - Post CSS (post.css)
  - Post JavaScript (post.js)

### min_chang_isaac
**역할**: Profile 관리 시스템 개발
- **담당 영역**:
  - Profile 관리 시스템 전체 (Controller, Entity, Repository, Service)
  - Profile 템플릿 2개 (profile.html, profile_edit.html)
  - Profile CSS (profilestyle.css)

### copilot-swe-agent[bot]
**역할**: AI 기반 개발 지원 및 문서화
- **커밋한 파일**:
  - Documentation: `.github/copilot-instructions.md`
  - Workflows: `.github/workflows/copilot-review.yml`
  - Bug Fixes: `ProfileController.java`, `foodboard.html`, `post.html`
- **주요 기여**:
  - GitHub Copilot Instructions 관리
  - 코드 리뷰 및 개선 제안
  - 개발 가이드라인 문서화

---

## 担当パートの概要（日本語）

各コントリビューターが使用した技術と注力したポイントを簡潔にまとめます。

### devman26173 — プロジェクト基盤・インフラ担当
以下のスニペットは、プロジェクト全体の起点となる `JoinApplication.java` を示している。`@SpringBootApplication` 一つで自動設定・コンポーネントスキャン・設定クラス読み込みが有効になり、`SpringApplication.run()` でサーブレットコンテナを起動する。この 1 クラスを土台に Docker・Maven・`application.properties` / `application-prod.yml` による環境別設定が組み合わさり、プロジェクト全体の運用基盤が構成されている。

```java
// JoinApplication.java — アプリケーションのエントリーポイント
@SpringBootApplication
public class JoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoinApplication.class, args);
	}
}
```

### devhyunju — FoodBoard システム担当
以下のスニペットは FoodBoard 機能の 3 つの核心を示す。① `FoodBoard.java` では `title`・`region`・`prefecture`・`rating`・`content`・`imageUrls`・`viewCount` のほか、DB に永続化しない `@Transient` フィールド（`likeCount`・`likedByMe`・`commentCount`）を持ち、`@PrePersist` で登録時刻を自動セットする。② `FoodBoardRepository.java` では地域別・都道府県別・複数都道府県・キーワード検索・ユーザー別 Top10 など実際に使用される全クエリを Spring Data JPA のメソッド命名で宣言している。③ `FileUploadController.java` では `POST /api/uploads/presign` で presigned URL を返し、`POST /api/uploads/image` で直接アップロードを受け付ける 2 つのエンドポイントを実装し、`PresignRequest` を Java record で定義している。

```java
// FoodBoard.java — 飲食情報を管理するエンティティ
@Entity
public class FoodBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String title;
    private String region;
    private String prefecture;
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String content;
    private String imageUrls;
    private LocalDateTime createdAt;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int viewCount = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Transient private Integer likeCount = 0;
    @Transient private Boolean likedByMe = false;
    @Transient private Integer commentCount = 0;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    // Getter & Setter ...
}

// FoodBoardRepository.java — 地域・都道府県別・検索など全クエリを Spring Data JPA で定義
public interface FoodBoardRepository extends JpaRepository<FoodBoard, Long> {
    List<FoodBoard> findAllByOrderByCreatedAtDesc();
    List<FoodBoard> findByRegionOrderByCreatedAtDesc(String region);
    List<FoodBoard> findByPrefectureOrderByCreatedAtDesc(String prefecture);
    List<FoodBoard> findByPrefectureInOrderByCreatedAtDesc(List<String> prefectures);
    List<FoodBoard> findTop10ByUser_UserIdOrderByCreatedAtDesc(Long userId);
    List<FoodBoard> findTop1ByPrefectureInOrderByCreatedAtDesc(List<String> prefectures);
    FoodBoard findFirstByRegionOrderByCreatedAtDesc(String region);
    List<FoodBoard> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(String title, String content);
    FoodBoard findTopByOrderByCreatedAtDesc();
}

// FileUploadController.java — presigned URL と直接アップロードの 2 エンドポイント
@RestController
@RequestMapping("/api/uploads")
public class FileUploadController {

    private final ImageUploadService imageUploadService;

    public FileUploadController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/presign")
    public ResponseEntity<?> createPresignedUploadUrl(@RequestBody PresignRequest request) {
        try {
            PresignedUpload upload = imageUploadService.createPresignedUpload(
                    request.fileName(), request.contentType(), request.fileSize());
            return ResponseEntity.ok(upload);
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "dev環境ではpresignアップロードをサポートしていません"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        UploadedImage uploadedImage = imageUploadService.uploadImage(file);
        return ResponseEntity.ok(uploadedImage);
    }

    public record PresignRequest(String fileName, String contentType, long fileSize) {}
}
```

### goodsujin — ユーザー認証・会員登録担当
以下のスニペットは認証フローの 2 つの中心的な実装を示す。① `UserService.registerUser()` では `userRepository.findByUsername()` で ID の重複を事前チェックし、重複があれば `IllegalArgumentException` をスローする。重複でなければ `username`・`name`・`region`・`prefecture` を全てセットしたうえで `passwordEncoder.encode()` で BCrypt ハッシュ化して保存する。`login()` では `passwordEncoder.matches()` で入力パスワードと DB のハッシュを照合し、一致すれば User を返す。② `UserController.loginSubmit()` では `returnUrl` パラメータを受け取り、ログイン成功後に元のページへ戻す動線を実装している。

```java
// UserService.java — 会員登録（重複チェック＋BCrypt）とログイン（matches 照合）
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String username, String name, String password,
                             String region, String prefecture) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("このIDは既に使用されています。");
        }
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setRegion(region);
        user.setPrefecture(prefecture);
        userRepository.save(user);
    }

    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    @Transactional
    public void withdrawUser(Long userId) {
        userRepository.deleteById(userId);
    }
}

// UserController.java — ログイン処理：returnUrl 対応＋セッション保存
@PostMapping("/login")
public String loginSubmit(
    @RequestParam String username,
    @RequestParam String password,
    @RequestParam(required = false) String returnUrl,
    HttpSession session,
    Model model
) {
    User user = userService.login(username, password);
    if (user != null) {
        session.setAttribute("loginUser", user);
        if (returnUrl != null && !returnUrl.isEmpty()) {
            return "redirect:" + returnUrl;
        }
        return "redirect:/board";
    }
    model.addAttribute("error", "IDまたはパスワードが一致しません");
    model.addAttribute("returnUrl", returnUrl);
    return "user-login";
}
```

### java0731kk — Post 掲示板・コメント・いいね機能担当
以下のスニペットは 3 種の機能の核心設計を示す。① `Comment.java` では `parentId` フィールドで大댓글の親子関係を表現し、`@Transient` な `likeCount`・`likedByMe`・`replies` でいいね数・いいね済みフラグ・返信ツリーをメモリ上で保持する。② `Like.java` では `targetType`（`"POST"` / `"COMMENT"` / `"REPLY"`）と `targetId` の組み合わせで投稿・コメント・返信のいいねを単一テーブルに統合管理し、コンストラクタで一括初期化できる。③ `LikeRepository.java` では重複チェック・件数カウント・全取得の 3 クエリを Spring Data JPA で宣言している。

```java
// Comment.java — 大댓글構造（parentId）＋いいね・返信を @Transient で保持
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;
    private Long parentId;      // 大댓글の場合、親コメントの ID
    private String content;
    private String author;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Transient private int likeCount;
    @Transient private boolean likedByMe;
    @Transient private List<Comment> replies = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }
    // Getter / Setter ...
}

// Like.java — POST / COMMENT / REPLY を targetType で統一管理
@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long targetId;
    private String targetType;  // "POST", "COMMENT", "REPLY"
    private String userId;

    public Like() {}

    public Like(Long targetId, String targetType, String userId) {
        this.targetId = targetId;
        this.targetType = targetType;
        this.userId = userId;
    }
    // Getter / Setter ...
}

// LikeRepository.java — 重複チェック・件数カウント・全取得の 3 クエリ
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByTargetIdAndTargetTypeAndUserId(Long targetId, String targetType, String userId);
    long countByTargetIdAndTargetType(Long targetId, String targetType);
    List<Like> findByTargetIdAndTargetType(Long targetId, String targetType);
}
```

### min_chang_isaac — プロフィール管理担当
以下のスニペットはプロフィール管理の全レイヤーを示す。① `Profile.java` では `@JsonIgnore` + `@OneToOne(fetch = FetchType.LAZY)` + `@JoinColumn(name = "user_id", unique = true)` で `User` と 1 対 1 に紐付け、`@Column` で `image_url`・`introduction`（最大 1000 文字）のカラム名を明示している。② `ProfileService` では `getByUserId()` が `orElseGet()` でプロフィール未作成ユーザーにも自動生成して返し、`updateProfile()` が introduction と imageUrl を上書き保存する。③ `ProfileController` では `GET /{userId}` でプロフィール・直近 10 件の FoodBoard・直近 10 件のコメントを Model に乗せて表示し、`POST /{userId}/edit` で編集内容を保存後にリダイレクトし、`POST /{userId}/withdraw` で会員脱退を処理する。

```java
// Profile.java — @JsonIgnore＋@OneToOne で User と 1 対 1 紐付け
@Entity
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "introduction", length = 1000)
    private String introduction;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    // Getter / Setter ...
}

// ProfileService.java — 未作成プロフィールの自動生成と更新処理
@Service
public class ProfileService {

    public Profile getByUserId(Long userId) {
        return profileRepository.findByUser_UserId(userId)
            .orElseGet(() -> {
                Profile p = new Profile();
                p.setUser(userRepository.findById(userId).orElseThrow());
                p.setIntroduction("");
                p.setImageUrl(null);
                return profileRepository.save(p);
            });
    }

    public void updateProfile(Long userId, Profile formProfile) {
        Profile profile = getByUserId(userId);
        profile.setIntroduction(formProfile.getIntroduction());
        profile.setImageUrl(formProfile.getImageUrl());
        profileRepository.save(profile);
    }
}

// ProfileController.java — 表示・編集・会員脱退の 3 機能
@Controller
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping("/{userId}")
    public String showProfile(@PathVariable Long userId, Model model) {
        model.addAttribute("profile", profileService.getByUserId(userId));
        model.addAttribute("boards", foodBoardRepository.findTop10ByUser_UserIdOrderByCreatedAtDesc(userId));
        model.addAttribute("comments", commentRepository.findTop10ByUser_UserIdOrderByCreatedAtDesc(userId));
        return "profile";
    }

    @GetMapping("/{userId}/edit")
    public String editForm(@PathVariable Long userId, Model model) {
        model.addAttribute("profile", profileService.getByUserId(userId));
        return "profile_edit";
    }

    @PostMapping("/{userId}/edit")
    public String editProfile(@PathVariable Long userId, Profile formProfile) {
        profileService.updateProfile(userId, formProfile);
        return "redirect:/profile/" + userId;
    }

    @PostMapping("/{userId}/withdraw")
    public String withdraw(@PathVariable Long userId, HttpSession session) {
        userService.withdrawUser(userId);
        session.invalidate();
        return "redirect:/login";
    }
}
```
