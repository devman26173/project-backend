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

- Login page, Signup page Frontend

</details>

<details>
<summary>キム・ヒョンジュ</summary>

- Board page

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

- 追後追加予定

</details>

<details>
<summary>キム・ヒョンジュ</summary>

- 追後追加予定

</details>

<details>
<summary>キム・ソンヒャン</summary>

- コメントやいいね機能を実装する際、未ログインのユーザーがボタンを押した場合の動作を設計する必要がありました。単にエラーを表示するだけでなく、「ログインページへ自然に誘導する」という処理を加えることで、ユーザーがストレスなく次のアクションに移れるよう工夫しました。また、いいね機能はAjaxによる非同期のトグル方式で実装し、ページを再読み込みせずに状態が即時反映されるようにすることで、操作性の向上を意識しました。この経験から、機能を動かすだけでなく、ユーザーの行動の流れを意識した設計の重要性を学びました。

</details>

<details>
<summary>ジャン・ミンチャン</summary>

- JWTへのマイグレーション

</details>
