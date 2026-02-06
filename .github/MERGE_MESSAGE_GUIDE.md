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

### 3. Auto-merge 사용 (권장)
PR에 `auto-merge` 라벨을 추가하면 자동으로 위 형식으로 merge됩니다.
(.github/workflows/auto-merge.yml 워크플로우 사용)

**작동 방식:**
1. PR에 `auto-merge` 라벨 추가
2. 워크플로우가 자동으로 실행되어 PR의 merge 가능 여부 확인
3. Merge 가능한 경우: 커스텀 메시지 형식으로 자동 merge 후 라벨 제거
4. Merge 실패 시: 라벨은 유지되고 실패 원인이 코멘트로 추가됨

**장점:**
- 수동으로 메시지 수정할 필요 없음
- 일관된 형식 보장
- Merge 실패 시 라벨이 유지되어 재시도 가능

## 주의사항
- GitHub의 기본 merge 메시지는 상황에 따라 다릅니다:
  - 같은 저장소 내 브랜치: `Merge pull request #{number} from {owner}/{branch}`
  - 포크된 저장소에서의 PR: `Merge pull request #{number} from {owner}/{repo}:{branch}`
- 반드시 수동으로 수정하여 `Merge PR/{owner}/{branch} (#{number})` 형식으로 변경해야 합니다.
- 일관된 형식 유지를 위해 모든 PR merge 시 이 가이드를 따라주세요.
- `auto-merge` 라벨 사용 시 권한이 있는 사용자만 추가하도록 주의하세요.
- Branch protection rules를 설정하여 merge 전 필수 리뷰를 요구하는 것을 권장합니다.

## Auto-merge 문제 해결

### 라벨이 유지되는 경우
`auto-merge` 라벨이 추가 후에도 남아있다면 merge가 실패한 것입니다. 다음을 확인하세요:

1. **Workflow 로그 확인**: Actions 탭에서 실패 원인 확인
2. **Merge 충돌**: 브랜치를 최신 상태로 업데이트
3. **Branch protection rules**: 필수 리뷰나 status checks 미완료
4. **권한 문제**: 워크플로우 권한 설정 확인

### 라벨이 제거되는 경우
Merge가 성공적으로 완료된 것입니다. PR이 닫히고 커스텀 메시지 형식으로 merge되었는지 확인하세요.

## 관련 파일
- `.github/PULL_REQUEST_TEMPLATE.md` - PR 템플릿 (merge 형식 가이드 포함)
- `.github/workflows/auto-merge.yml` - 자동 merge 워크플로우
