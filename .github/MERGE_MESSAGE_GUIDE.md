# Merge 메시지 형식 가이드

## 개요
이 저장소에서는 PR merge 시 일관된 커밋 메시지 형식을 사용합니다.

## Merge 메시지 형식

### 표준 형식
```
Merge PR/{owner}/{branch_name} (#{pr_number})
```

### 예시
```
Merge PR/devman26173/feature/button_of_home_page (#128)
Merge PR/goodsujin/feature/user-login (#100)
Merge PR/java0731kk/fix/post-comment-bug (#85)
```

## GitHub에서 Merge하는 방법

### 1. Squash and Merge (권장)
1. PR 페이지에서 "Squash and merge" 버튼 클릭
2. 커밋 메시지를 위의 형식으로 수정
3. "Confirm squash and merge" 클릭

### 2. Merge Commit
1. PR 페이지에서 "Merge pull request" 버튼 클릭
2. "Edit" 버튼을 클릭하여 커밋 메시지 수정
3. 위의 형식으로 변경
4. "Confirm merge" 클릭

### 3. Auto-merge 사용
PR에 `auto-merge` 라벨을 추가하면 자동으로 위 형식으로 merge됩니다.
(.github/workflows/auto-merge.yml 워크플로우 사용)

## 주의사항
- 기본 merge 메시지가 `Merge pull request #{number} from {owner}/{branch}` 형식으로 생성됩니다 (fork의 경우 `{owner}/{repo}:{branch}` 포함).
- 반드시 수동으로 수정하여 `Merge PR/{owner}/{branch} (#{number})` 형식으로 변경해야 합니다.
- 일관된 형식 유지를 위해 모든 PR merge 시 이 가이드를 따라주세요.
- `auto-merge` 라벨 사용 시 권한이 있는 사용자만 추가하도록 주의하세요.
- Branch protection rules를 설정하여 merge 전 필수 리뷰를 요구하는 것을 권장합니다.

## 관련 파일
- `.github/PULL_REQUEST_TEMPLATE.md` - PR 템플릿 (merge 형식 가이드 포함)
- `.github/workflows/auto-merge.yml` - 자동 merge 워크플로우
