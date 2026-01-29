# PR 리뷰 자동화 가이드

## 개요
이 저장소는 팀원별로 담당 영역이 지정되어 있으며, PR 생성 시 자동으로 해당 파일의 담당자가 리뷰어로 지정됩니다.

## 담당 영역

| 팀원 | 담당 영역 | 파일 패턴 |
|------|----------|----------|
| 선향 | Post 관련 | Post*.java, post*.* |
| 현주 | FoodBoard 관련 | FoodBoard*.java, foodboard*.* |
| 수진 | User, Signup, Login 관련 | User*.java, user*.*, *login*.*, *signup*.* |
| 민창 | Profile 관련 | Profile*.java, profile*.* |

## 작동 방식

### 1. CODEOWNERS 파일
`.github/CODEOWNERS` 파일에 정의된 규칙에 따라 PR 생성 시 자동으로 리뷰어가 지정됩니다.

**예시:**
- `PostController.java`를 수정하면 → @seonhyang이 자동으로 리뷰어로 추가됨
- `UserService.java`를 수정하면 → @sujin이 자동으로 리뷰어로 추가됨

### 2. PR 템플릿
PR 생성 시 자동으로 로드되는 템플릿이 제공됩니다:
- 변경된 파일 영역 체크리스트
- 담당 외 파일 수정 시 이유 명시 섹션
- 테스트 완료 체크리스트

### 3. 자동 파일 변경 체크 (GitHub Actions)
PR이 생성되거나 업데이트될 때마다 자동으로:
1. 변경된 파일을 담당자별로 분류
2. 여러 담당 영역이 포함되었는지 확인
3. PR에 코멘트로 변경 내역 요약 제공

## PR 생성 가이드

### 담당 영역만 수정하는 경우
1. 평소처럼 PR 생성
2. 자동으로 담당자가 리뷰어로 지정됨
3. PR 템플릿의 체크리스트 작성

### 담당 외 파일을 수정하는 경우
1. PR 생성 시 템플릿의 "담당 외 파일 수정 여부" 섹션을 반드시 작성
2. 수정한 파일 목록과 이유를 명시
3. 해당 파일의 담당자가 자동으로 리뷰어로 추가됨

**예시:**
```markdown
## 담당 외 파일 수정 여부
- [x] 담당 외 파일을 수정했습니다
  - 수정한 파일: UserService.java, ProfileService.java
  - 수정 이유: Post 기능과 User/Profile 기능 간의 연동을 위해 공통 메서드 추가
```

## GitHub Actions 워크플로우

PR 생성/업데이트 시 자동으로 실행되며, 다음과 같은 코멘트를 생성합니다:

```markdown
## 📋 PR 파일 변경 내역

이 PR에서 수정된 파일들을 담당자별로 분류했습니다.

### 🔵 Post 관련 파일 (담당: 선향)
```
src/main/java/com/example/join/controller/PostController.java
src/main/resources/static/css/post.css
```

### 🟡 User/Login/Signup 관련 파일 (담당: 수진)
```
src/main/java/com/example/join/service/UserService.java
```

---
⚠️ **주의**: 이 PR은 여러 담당 영역의 파일을 수정하거나 공통 파일을 포함합니다.
해당 영역의 담당자들이 리뷰를 진행해주세요.
```

## 주의사항

1. **담당 외 파일 수정 시 사전 논의 권장**
   - 다른 팀원의 담당 영역을 수정할 때는 사전에 논의하는 것이 좋습니다.

2. **PR 설명 작성 필수**
   - 특히 여러 영역의 파일을 수정했을 때는 상세한 설명이 필요합니다.

3. **리뷰어 확인**
   - PR 생성 후 올바른 담당자가 리뷰어로 지정되었는지 확인하세요.

4. **공통 파일**
   - 누구의 담당 영역에도 속하지 않는 파일(설정 파일, 공통 유틸 등)은
     팀 리더 또는 전체 팀원의 리뷰가 필요할 수 있습니다.

## 문제 해결

### Q: 리뷰어가 자동으로 지정되지 않아요
A: GitHub 저장소 설정에서 "Code owners" 기능이 활성화되어 있는지 확인하세요.
   Settings → Branches → Branch protection rules에서 "Require review from Code Owners"를 활성화하세요.

### Q: 워크플로우가 실행되지 않아요
A: GitHub Actions가 활성화되어 있는지 확인하세요.
   Settings → Actions → General → "Allow all actions and reusable workflows"가 선택되어 있어야 합니다.

### Q: CODEOWNERS 파일을 수정하려면?
A: `.github/CODEOWNERS` 파일을 직접 수정하고 PR을 생성하세요.
   팀 전체의 리뷰를 받는 것이 좋습니다.

## 참고 자료
- [GitHub CODEOWNERS 문서](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/about-code-owners)
- [GitHub Actions 문서](https://docs.github.com/en/actions)
- [Pull Request 템플릿 사용법](https://docs.github.com/en/communities/using-templates-to-encourage-useful-issues-and-pull-requests/creating-a-pull-request-template-for-your-repository)

