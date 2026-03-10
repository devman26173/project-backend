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

- Board page

</details>

<details>
<summary>キ・ソンヒャン</summary>

- Comment area

</details>

<details>
<summary>ジャン・ミンチャン</summary>

- Profile Area(ユーザーごとProfileを保持)
- ERD(DBリレーションの基本設計)
- ProfileのViewからBoardの投稿とコメントが表示する機能の実装


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
<summary>キ・ソンヒャン</summary>

- 追後追加予定

</details>

<details>
<summary>ジャン・ミンチャン</summary>

- JWTへのマイグレーション

</details>
