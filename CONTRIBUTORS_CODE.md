# 기여자별 주요 코드 스니펫

각 기여자가 담당한 기능의 핵심 코드를 파트별로 정리합니다.

---

## 1. devman26173 — 프로젝트 아키텍처 & 보안 설정

**담당**: Spring Boot 초기 설정, Spring Security, 전체 인프라 구성

```java
// src/main/java/com/example/join/config/SecurityConfig.java

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt: 단방향 해시 암호화 (복호화 불가)
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 보호 비활성화 (POST 요청 허용)
            .csrf(csrf -> csrf.disable())
            // 모든 URL 접근 허용 (로그인 체크는 컨트롤러에서 직접 처리)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // 기본 로그인/로그아웃 폼 비활성화 → user-login.html 사용
            .formLogin(form -> form.disable())
            .logout(logout -> logout.disable())
            // H2 콘솔 iframe 차단 해제
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable())
            );
        return http.build();
    }
}
```

---

## 2. devhyunju — FoodBoard 음식 게시판 시스템

**담당**: 게시판 CRUD, 지역 필터링, 댓글/좋아요 수 조회

```java
// src/main/java/com/example/join/controller/FoodBoardController.java

@Controller
public class FoodBoardController {

    @GetMapping("/board")
    public String home(@RequestParam(required = false) String region,
                       @RequestParam(required = false) String prefecture,
                       HttpSession session, Model model) {

        List<FoodBoard> boards;

        // 지역(도도부현/광역) 필터링
        if (prefecture != null && !prefecture.isEmpty()) {
            boards = foodBoardService.findByPrefecture(prefecture);
            model.addAttribute("currentCategory", prefecture);
        } else if (region != null && !region.isEmpty()) {
            boards = foodBoardService.findByRegion(region);
            model.addAttribute("currentCategory", region);
        } else {
            boards = foodBoardService.findAll();
            model.addAttribute("currentCategory", "すべて");
        }

        User loginUser = (User) session.getAttribute("loginUser");
        String currentUserId = loginUser != null ? loginUser.getUsername() : null;

        // 각 게시글의 댓글 수(부모 댓글만)와 좋아요 수 세팅
        for (FoodBoard board : boards) {
            long commentCount = commentRepository.countByPostIdAndParentIdIsNull(board.getId());
            board.setCommentCount((int) commentCount);
            board.setLikeCount(postService.getLikeCount(board.getId(), "BOARD"));
            board.setLikedByMe(currentUserId != null &&
                postService.isLiked(board.getId(), "BOARD", currentUserId));
        }

        model.addAttribute("boards", boards);
        return "foodboard";
    }
}
```

---

## 3. goodsujin — 사용자 인증 시스템 (로그인 / 회원가입)

**담당**: 로그인·회원가입·비밀번호 변경·회원탈퇴 전체 흐름

```java
// src/main/java/com/example/join/service/UserService.java

@Service
public class UserService {

    // 회원가입: 중복 ID 검사 후 BCrypt 암호화 저장
    public void registerUser(String username, String name, String password,
                             String region, String prefecture) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("このIDは既に使用されています。");
        }
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));  // 암호화
        user.setRegion(region);
        user.setPrefecture(prefecture);
        userRepository.save(user);
    }

    // 로그인: DB에서 사용자 조회 후 BCrypt 비밀번호 검증
    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;  // 로그인 성공
            }
        }
        return null;  // 로그인 실패
    }

    // 비밀번호 변경: 암호화 후 DB 업데이트
    @Transactional
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // 회원탈퇴
    @Transactional
    public void withdrawUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
```

---

## 4. java0731kk — Post 게시판 & 댓글 / 좋아요 기능

**담당**: 게시글·댓글 저장/조회, 좋아요 토글 및 카운트

```java
// src/main/java/com/example/join/service/PostService.java  (좋아요 기능)
// src/main/java/com/example/join/service/CommentService.java (댓글 기능)

@Service
public class PostService {

    // 좋아요 토글: 이미 눌렀으면 취소, 안 눌렀으면 추가
    public void toggleLike(Long targetId, String targetType, String userId) {
        Optional<Like> existingLike = likeRepository
            .findByTargetIdAndTargetTypeAndUserId(targetId, targetType, userId);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());   // 좋아요 취소
        } else {
            likeRepository.save(new Like(targetId, targetType, userId));  // 좋아요 추가
        }
    }

    // 좋아요 수 조회
    public int getLikeCount(Long targetId, String targetType) {
        return (int) likeRepository.countByTargetIdAndTargetType(targetId, targetType);
    }

    // 좋아요 여부 확인
    public boolean isLiked(Long targetId, String targetType, String userId) {
        return likeRepository
            .findByTargetIdAndTargetTypeAndUserId(targetId, targetType, userId)
            .isPresent();
    }
}

@Service
public class CommentService {

    // 최상위 댓글 조회 (대댓글 제외)
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostIdAndParentIdIsNull(postId);
    }

    // 대댓글 조회
    public List<Comment> findRepliesByParentId(Long parentId) {
        return commentRepository.findByParentId(parentId);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}
```

---

## 5. min_chang_isaac — 프로필 관리 시스템

**담당**: 프로필 조회·수정, 최근 게시글·댓글 표시

```java
// src/main/java/com/example/join/controller/ProfileController.java
// src/main/java/com/example/join/service/ProfileService.java

@Controller
@RequestMapping("/profile")
public class ProfileController {

    // 현재 로그인 유저 프로필 페이지로 리다이렉트
    @GetMapping
    public String myProfile(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";
        return "redirect:/profile/" + loginUser.getUserId();
    }

    // 프로필 조회: 최근 게시글 10개 + 최근 댓글 10개 함께 표시
    @GetMapping("/{userId}")
    public String showProfile(@PathVariable Long userId, Model model) {
        Profile profile = profileService.getByUserId(userId);
        List<FoodBoard> boards  = foodBoardRepository
            .findTop10ByUser_UserIdOrderByCreatedAtDesc(userId);
        List<Comment>  comments = commentRepository
            .findTop10ByUser_UserIdOrderByCreatedAtDesc(userId);

        model.addAttribute("profile",  profile);
        model.addAttribute("boards",   boards);
        model.addAttribute("comments", comments);
        return "profile";
    }

    // 프로필 수정 저장
    @PostMapping("/{userId}/edit")
    public String editProfile(@PathVariable Long userId, Profile formProfile) {
        profileService.updateProfile(userId, formProfile);
        return "redirect:/profile/" + userId;
    }
}

@Service
public class ProfileService {

    // 프로필이 없으면 빈 프로필 자동 생성
    public Profile getByUserId(Long userId) {
        return profileRepository.findByUser_UserId(userId)
            .orElseGet(() -> {
                Profile p = new Profile();
                p.setUser(userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。")));
                p.setIntroduction("");
                p.setImageUrl(null);
                return profileRepository.save(p);
            });
    }

    // 소개글 및 이미지 URL 업데이트
    public void updateProfile(Long userId, Profile formProfile) {
        Profile profile = getByUserId(userId);
        profile.setIntroduction(formProfile.getIntroduction());
        profile.setImageUrl(formProfile.getImageUrl());
        profileRepository.save(profile);
    }
}
```
