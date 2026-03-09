package com.example.join.config;

import com.example.join.service.AiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.ai.openai.chat.enabled", havingValue = "true", matchIfMissing = true)
public class AiServiceConfig {

    @Bean
    public AiService aiService(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        return new AiService(chatClientBuilder, objectMapper);
    }
}
