package com.example.join.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AiService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public AiService(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public AiReply generateResponse(String question) {
        try {
            String content = chatClient
                .prompt()
                .system("""
                    あなたは日本のグルメ掲示板向けアシスタントです。
                    ユーザーの質問を理解し、必ず日本語で短く自然に回答してください。
                    さらに、foodboard検索に使いやすい日本語キーワードを3個から5個抽出してください。
                    出力は必ずJSONのみで返してください。説明文やMarkdownは不要です。
                    形式:
                    {"answer":"日本語の回答","keywords":["キーワード1","キーワード2","キーワード3"]}
                    """)
                .user(question)
                .call()
                .content();
            return parseReply(content);
        } catch (Exception e) {
            log.error("AI API 호출 중 오류 발생", e);
            return new AiReply("回答を生成する途中でエラーが発生しました。", List.of());
        }
    }

    private AiReply parseReply(String content) {
        try {
            String normalized = content
                .replace("```json", "")
                .replace("```", "")
                .trim();

            AiReply parsed = objectMapper.readValue(normalized, AiReply.class);
            List<String> keywords = parsed.keywords() == null ? List.of() : parsed.keywords().stream()
                .filter(keyword -> keyword != null && !keyword.isBlank())
                .map(String::trim)
                .distinct()
                .limit(5)
                .toList();

            String answer = parsed.answer() == null || parsed.answer().isBlank()
                ? "おすすめ情報をまとめました。"
                : parsed.answer().trim();

            return new AiReply(answer, keywords);
        } catch (Exception e) {
            log.warn("AI 응답 JSON 파싱 실패, 원문을 그대로 사용합니다. content={}", content, e);
            return new AiReply(content.trim(), List.of());
        }
    }

    public record AiReply(String answer, List<String> keywords) {
    }
}
