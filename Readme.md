# 日本の地域別コミュニティ
# J-BOARD

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
- AWS S3(imageファイル管理)

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

- Login page, Signup page Frontend
-
-
-

</details>

<details>
<summary>キム・ヒョンジュ</summary>

- Board page
-
-
-

</details>

<details>
<summary>キ・ソンヒャン</summary>

- Comment area
-
-
-

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
-
-
-
- -
- -
- -

</details>

<details>
<summary>カン・スジン</summary>

-
-
-
-

</details>

<details>
<summary>キム・ヒョンジュ</summary>

-
-
-
-

</details>

<details>
<summary>キ・ソンヒャン</summary>
-
-
-
-

</details>

<details>
<summary>ジャン・ミンチャン</summary>
- JPAの利点とSpringBootの長所の体験
- DB設計の重要性を体験
-
-

</details>

## 課題

<details>
<summary>キム・テイヒョン</summary>

- チームメンバーのスケジュールの管理方法
-
-
-
-
-
-

</details>

<details>
<summary>カン・スジン</summary>

-
-
-
-

</details>

<details>
<summary>キ・ヒョンジュ</summary>

-
-
-
-

</details>

<details>
<summary>キム・ソンヒャン</summary>

-
-
-
-

</details>

<details>
<summary>ジャン・ミンチャン</summary>
-JWTへのマイグレーション
-
-
-

</details>

## 今後の対応

<details>
<summary>キム・テイヒョン</summary>

-
-
-
-
-
-
-

</details>

<details>
<summary>カン・スジン</summary>

-
-
-
-

</details>

<details>
<summary>キム・ヒョンジュ</summary>

-
-
-
-

</details>

<details>
<summary>キ・ソンヒャン</summary>

-
-
-
-

</details>

<details>
<summary>ジャン・ミンチャン</summary>
【感想】
・アーキテクチャ層において、ORMがもつ役割の重要さを学びました。
・ERD設計は重要なことですが、プロジェクトの初期段階では設計しすぎると開発の妨げにもなれることを体験しました。ERDの更新と実装後の振り返りの重要さを覚えられました。

・これまではユーザーとしてWebを利用する立場だったため、バックエンドのロジックを考える際に、ついメモリと画面表示の遅延に意識が向いていましたが、今回の開発を通じて、システムの設計においてはデーターの整合性を保つことが非常に重要であるという点を学べられました。
