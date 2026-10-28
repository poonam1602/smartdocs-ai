package com.smartdocs.smartdocs;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Value("${langchain4j.open-ai.chat-model.api-key}")
    private String groqApiKey;

    @Value("${langchain4j.google-ai-gemini.api-key}")
    private String geminiApiKey;

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(groqApiKey)
                .baseUrl("https://api.groq.com/openai/v1")
                .modelName("openai/gpt-oss-120b")
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(geminiApiKey)
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/openai/")
                .modelName("gemini-embedding-001") // Updated to supported model
                .build();
    }
}