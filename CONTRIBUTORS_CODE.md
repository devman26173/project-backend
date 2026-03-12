# 기여자별 주요 코드 스니펫

각 기여자가 담당한 기능의 핵심 코드를 파트별로 정리합니다.

---

## 1. devman26173 — Home / Spring AI / Error 페이지

**담당**: 홈 화면, Spring AI 질문 API, 에러 페이지

```java
// HomeController.java — Spring AI 질문 API (POST /api/ai/ask)
@PostMapping("/api/ai/ask") @ResponseBody
public ResponseEntity<Map<String, Object>> askAi(@RequestBody Map<String, String> request) {
    String question = request.getOrDefault("question", "");  // null 안전 처리
    AiService.AiReply reply = aiService.generateResponse(question.trim());
    return ResponseEntity.ok(Map.of("answer", reply.answer(), "keywords", reply.keywords()));
}
```

```java
// AiService.java — GPT 응답 생성 (Spring AI ChatClient)
public AiReply generateResponse(String question) {
    String content = chatClient.prompt()
        .system("あなたは日本のグルメ掲示板向けアシスタントです。必ずJSONで返答。")
        .user(question).call().content();
    return parseReply(content);  // JSON → AiReply(answer, keywords)
}
```

```java
// TestErrorController.java — 에러 페이지 테스트 (GET /test/500)
@GetMapping("/test/500")
public String trigger500() {
    throw new RuntimeException("forced 500 for browser test");
}
```

---

## 2. devhyunju — FoodBoard 음식 게시판 시스템

**담당**: 게시판 CRUD, 지역 필터링, 댓글/좋아요 수 조회

```java
// FoodBoardController.java — 지역 필터링 + 댓글/좋아요 집계 (GET /board)
List<FoodBoard> boards = prefecture != null ? foodBoardService.findByPrefecture(prefecture)
    : region != null ? foodBoardService.findByRegion(region) : foodBoardService.findAll();
boards.forEach(b -> {
    b.setCommentCount((int) commentRepository.countByPostIdAndParentIdIsNull(b.getId()));
    b.setLikeCount(postService.getLikeCount(b.getId(), "BOARD"));
    b.setLikedByMe(postService.isLiked(b.getId(), "BOARD", currentUserId));
});
```

---

## 3. goodsujin — 사용자 인증 시스템 (로그인 / 회원가입)

**담당**: 로그인·회원가입·비밀번호 변경·회원탈퇴 전체 흐름

```java
// UserService.java — 회원가입 (중복 체크 + BCrypt 암호화)
public void registerUser(String username, String name, String password, String region, String prefecture) {
    if (userRepository.findByUsername(username).isPresent())
        throw new IllegalArgumentException("このIDは既に使用されています。");
    User user = new User();
    user.setUsername(username);  user.setName(name);
    user.setPassword(passwordEncoder.encode(password));  // BCrypt 암호화
    user.setRegion(region);  user.setPrefecture(prefecture);  userRepository.save(user);
}
```

---

## 4. java0731kk — Post 게시판 & 댓글 / 좋아요 기능

**담당**: 게시글·댓글 저장/조회, 좋아요 토글 및 카운트

```java
// PostService.java — 좋아요 토글 (추가 / 취소)
public void toggleLike(Long targetId, String targetType, String userId) {
    Optional<Like> like = likeRepository
        .findByTargetIdAndTargetTypeAndUserId(targetId, targetType, userId);
    if (like.isPresent()) likeRepository.delete(like.get());       // 좋아요 취소
    else likeRepository.save(new Like(targetId, targetType, userId));  // 좋아요 추가
}
```

---

## 5. min_chang_isaac — 프로필 관리 시스템

**담당**: 프로필 조회·수정, 최근 게시글·댓글 표시

```java
// ProfileService.java — 프로필 조회 (없으면 빈 프로필 자동 생성)
public Profile getByUserId(Long userId) {
    return profileRepository.findByUser_UserId(userId).orElseGet(() -> {
        Profile p = new Profile();
        p.setUser(userRepository.findById(userId).orElseThrow());
        p.setIntroduction("");  p.setImageUrl(null);
        return profileRepository.save(p);
    });
}
```
