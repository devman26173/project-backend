# J-BOARD
- 日本の地域別コミュニティ

## 概要

本プロジェクトは、日本各地別々のコミュニティを想定して開発しました。

Spring BootとThymeleaf を用いたフロントエンド構成となっています。

## 機能一覧

- ユーザー登録機能
- ログイン／ログアウト機能
- 投稿作成・編集・削除機能
- コメント投稿機能
- 投稿一覧／詳細表示機能

## 技術構成

### Backend

- Java 21
- Spring Boot
- JPA（Hibernate）

### Frontend

- Thymeleaf
- Bootstrap CSS
- HTML, CSS, JS

### Database

- MySQL
- H2（ローカル環境）

### その他

- GitHub
- Slack
- Notion
- AWS EC2
- AWS S3 (imageファイル管理)

## システム構成


[Client] Thymeleaf

↓ REST API
      
[Server] Spring Boot

↓
      
[Database] MySQL


## 담당 파트 (한국어)

<details>
<summary>devman26173 - 프로젝트 총괄 및 인프라</summary>

- 프로젝트 초기 설정 및 전체 아키텍처 구성
- Docker, Maven 빌드 환경 구성
- 홈 페이지, 게시판 목록 페이지, 메뉴바, 에러 페이지 개발
- 일정 관리 및 Git 브랜치 관리 (merge 담당)

</details>

<details>
<summary>goodsujin (강수진) - 사용자 인증 시스템</summary>

- 회원가입 및 로그인/로그아웃 기능 구현
- 비밀번호 암호화 처리
- 회원 탈퇴 및 비밀번호 변경 기능 구현
- User/SignupForm 관련 Controller, Entity, Service, Repository 전체 레이어 담당

</details>

<details>
<summary>devhyunju (기현주) - 게시판(FoodBoard) 시스템</summary>

- 게시판 CRUD 기능 구현 (작성·수정·삭제·조회)
- 지도 정보를 활용한 지역 필터링 기능 구현
- 키워드 검색 기능 구현 (제목 및 내용 동시 검색)
- 공지사항 페이지 작성
- 파일 업로드 기능 구현

</details>

<details>
<summary>java0731kk (김성향) - 게시글 댓글 및 좋아요 기능</summary>

- 게시글 댓글 및 대댓글(중첩 구조) 작성 기능 구현
- 댓글 정렬 기능 구현 (최신순/오래된순)
- 작성자 본인만 댓글 편집·삭제 가능한 권한 제어 구현
- 비로그인 상태에서 접근 시 알림 및 로그인 페이지 유도
- 좋아요 기능을 Ajax 비동기 토글 방식으로 구현

</details>

<details>
<summary>min_chang_isaac (장민창) - 프로필 관리 시스템</summary>

- ERD 설계 및 데이터베이스 관계 정의
- 사용자별 프로필 정보 관리 기능 구현
- 프로필 화면에서 해당 사용자의 게시글 및 댓글 조회 기능 구현
- Profile 관련 Controller, Entity, Repository, Service 전체 레이어 담당

</details>

---

## 担当範囲

<details>
<summary>キム・テイヒョン</summary>

- 機能明細書 作成
- プロジェクト ファイル 管理（初期化、Git merge）
- 日程管理、ガイド 文書 作成
- Home page, Board page, Menu bar, Error page 開発

</details>

<details>
<summary>カン・スジン</summary>

- Login, Logout, signup, singout, password変更, password暗号化

</details>

<details>
<summary>キ・ヒョンジュ</summary>

- 担当業務：掲示板機能の全般的な実装

- 掲示板の基本機能（CRUD）の実装
- 地図情報を活用した地域フィルタリング機能の構築
- 検索機能の実装
- お知らせ（公知事項）ページの作成

</details>

<details>
<summary>キム・ソンヒャン</summary>

- Comment area
- 掲示板投稿に対するコメント・返信（入れ子構造）の投稿機能を実装
- コメント一覧を新着順・古い順に並べ替えるソート機能を実装
- 投稿者本人のみ編集・削除が可能となる権限制御を実装
- 未ログイン状態での操作時にアラートを表示し、ログイン画面へ遷移させるアクセス制御を実装
- いいね機能を非同期処理（Ajax）によるトグル方式で実装

</details>

<details>
<summary>ジャン・ミンチャン</summary>

- Profile area
- ERD(プロジェクトのリレーション設計)
- ユーザーごとプロフィール情報を保持する機能
- ProfileのViewからBoardの投稿とコメントが表示する機能
- SQLのCRUDを用いて機能テストおよび運用


</details>

## 工夫点

<details>
<summary>キム・テイヒョン</summary>

- Spring boot 復習
- 追後追加予定

</details>

<details>
<summary>カン・スジン</summary>

- ログイン機能の実装中、認証エラーの原因を正確に特定するため、処理過程を段階別に細分化して検証しました。具体的には、DB内でのID照合からパス워드の一致判定まで、各段階の実行結果とデータ比較内容をコンソールに出力するように実装しました。これにより、どの段階で論理的なエラーが発生しているかを可視化し、問題を解決することができました。このようなアプローチは、複雑なシステムエラーを論리的に追跡し解決する上で非常に役立ちました。

</details>

<details>
<summary>キ・ヒョンジュ</summary>

- 今回のプロジェクトでは、掲示板の基本機能であるCRUD、検索、フィルタリング機能を完成させることに注力しました。特にフィルタリングと検索機能を実装する際、どのように構築すべきか深く悩みました。フィルタリングについては、日本の地方と都道府県を地図のようにあらかじめMapで整理し、JPAのInクエリを使うことでエラーを解決しました。検索については、タイトルと内容のどちらにキーワードが含まれていてもヒットするように、ContainingとORを使用して利便性を高めました。最初は「自分にできるだろうか」という不安もありましたが、掲示板の基本機能をしっかりと形にできたことに大きな達成感を感じています。

</details>

<details>
<summary>キム・ソンヒャン</summary>

- コメントやいいね機能を実装する際、未ログインのユーザーがボタンを押した場合の動作を設計する必要がありました。単にエラーを表示するだけでなく、「ログインページへ自然に誘導する」という処理を加えることで、ユーザーがストレスなく次のアクションに移れるよう工夫しました。また、いいね機能はAjaxによる非同期のトグル方式で実装し、ページを再読み込みせずに状態が即時反映されるようにすることで、操作性の向上を意識しました。この経験から、機能を動かすだけでなく、ユーザーの行動の流れを意識した設計の重要性を学びました。

</details>

<details>
<summary>ジャン・ミンチャン</summary>

- ビジネスロジックに適合するERDやクラスの設計検討

</details>
