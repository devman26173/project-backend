# 日本の地域別コミュニティ
## チームメンバー
‐ リーダー：キム・テヒョン
<br>
‐ 선향：Post 관련 파일 담당
<br>
‐ 현주：FoodBoard 관련 파일 담당
<br>
‐ 수진：User, Signup, Login 관련 파일 담당
<br>
‐ 민창：Profile 관련 파일 담당
<br>

## 技術スタック
‐ Frontend：React
<br>
‐ Backend：Spring boot
<br>
‐ DB：MySQL

## PR 리뷰 규칙
이 프로젝트는 팀원별로 담당 영역이 지정되어 있습니다:
- **선향**: Post 관련 파일 (Post*.java, post*.*)
- **현주**: FoodBoard 관련 파일 (FoodBoard*.java, foodboard*.*)
- **수진**: User, Signup, Login 관련 파일 (User*.java, user*.*, *login*.*, *signup*.*)
- **민창**: Profile 관련 파일 (Profile*.java, profile*.*)

### CODEOWNERS
`.github/CODEOWNERS` 파일에 각 담당자의 영역이 정의되어 있습니다.
PR 생성 시 변경된 파일의 소유자가 자동으로 리뷰어로 지정됩니다.

### 자동 파일 변경 체크
PR이 생성되거나 업데이트될 때, GitHub Actions가 자동으로:
1. 변경된 파일을 담당자별로 분류
2. 여러 담당 영역의 파일이 수정되었는지 확인
3. PR에 코멘트로 변경 내역 요약 제공

**담당 외 파일을 수정하는 경우**, 해당 파일의 담당자가 자동으로 리뷰어로 추가되며,
PR 템플릿에 수정 이유를 명시해주세요.
