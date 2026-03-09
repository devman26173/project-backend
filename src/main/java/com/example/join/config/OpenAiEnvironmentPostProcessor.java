package com.example.join.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;

import java.util.Map;

public class OpenAiEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String apiKey = environment.getProperty("spring.ai.openai.api-key", "");
        if (!StringUtils.hasText(apiKey)) {
            environment.getPropertySources().addFirst(
                new MapPropertySource("openai-disabled", Map.of("spring.ai.openai.chat.enabled", "false"))
            );
        }
    }
}
