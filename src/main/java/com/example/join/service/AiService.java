package com.example.join.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AiService {
    
    private final ChatClient chatClient;
    
    public AiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
    
    public String generateResponse(String question) {
        try {
            String prompt = String.format(
                "일본에 관한 질문에 대해 간단하고 도움이 되는 답변을 한국어로 해주세요. 질문: %s", 
                question
            );
            
            return chatClient
                .prompt(prompt)
                .call()
                .content();
            
        } catch (Exception e) {
            log.error("AI API 호출 중 오류 발생: ", e);
            return "답변을 생성하는 중 오류가 발생했습니다.";
        }
    }
}