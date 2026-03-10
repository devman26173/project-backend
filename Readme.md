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


## 機能明細

| 画面（メニュー） | 機能名 | 使用DB（ドメイン） | 機能（概要） | Path | Method | 完了 | 担当者 |
|---|---|---|---|---|---|---|---|
| ホーム | ホーム画面表示 | （なし） | 地域ドロップダウンと日本地図画像を表示する。ログイン状態によりボタン表示を切り替える。 | / | GET | ✅ | テイヒョン |
| ホーム | 地域別掲示板一覧へ遷移 | FoodBoard | 地域別グルメ掲示板の一覧ページへ移動する。各地域の代表投稿を表示する。 | /boards | GET | ✅ | テイヒョン |
| エラーページ | 404エラーページ表示 | （なし） | 存在しないページへのアクセス時に404エラー画面を表示する。 | /error/404 | GET | ✅ | テイヒョン |
| エラーページ | 500エラーページ表示 | （なし） | サーバーエラー発生時に500エラー画面を表示し、ログに記録する。 | /error/500 | GET | ✅ | テイヒョン |
| 各ページ（追加機能） | メニューバー表示 | （UI） | 全ページ下部にナビゲーションバーを表示する。ホーム・掲示板・プロフィールへのリンクを提供する。 | （UI） | （UI） | ✅ | テイヒョン |
| 各ページ（追加機能） | ページ遷移エフェクト | （UI） | ページ移動時にスライドアニメーションを表示する。 | （UI） | （UI） | ✅ | テイヒョン |
| ホーム（追加機能） | AI質問機能（ChatGPT） | （なし） | ユーザーの質問をChatGPT APIに送信し、日本語で回答と検索キーワードを返す。 | /api/ai/ask | POST | ✅ | テイヒョン |
| ホーム（追加機能） | AIレスポンスキャッシュ | （なし） | OpenAI APIトークン節約のため、Redisで1時間のレスポンスキャッシュを設定する。 | （なし） | （なし） | ✅ | テイヒョン |
| ホーム（追加機能） | ウェルカムメッセージトグル | （UI） | ホーム画面でウェルカムメッセージの表示・非表示を切り替える。 | （UI） | （UI） | ✅ | テイヒョン |
| 掲示板（追加機能） | 画像R2直接アップロード（Presigned URL） | （なし） | Cloudflare R2への署名付きURLを発行し、クライアントから直接アップロードする。 | /api/uploads/presign | POST | ✅ | テイヒョン |
| 掲示板（追加機能） | 画像アップロード（サーバー経由） | （なし） | サーバー経由でCloudflare R2に画像をアップロードする。 | /api/uploads/image | POST | ✅ | テイヒョン |

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

- 追後追加予定

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
