package com.smartdocs.smartdocs;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final DocumentChunkRepository repository;
    private final EmbeddingModel embeddingModel;
    private final ChatLanguageModel chatModel;

    public ChatService(DocumentChunkRepository repository, EmbeddingModel embeddingModel, ChatLanguageModel chatModel) {
        this.repository = repository;
        this.embeddingModel = embeddingModel;
        this.chatModel = chatModel;
    }

    public String askQuestion(String question) {
        // 1. Get embedding for the question
        Embedding questionEmbedding = embeddingModel.embed(question).content();
        String vectorString = Arrays.toString(questionEmbedding.vector());

        // 2. Find similar chunks in DB
        List<DocumentChunk> similarChunks = repository.findTop3Similar(vectorString);

        // 3. Combine context
        String context = similarChunks.stream()
                .map(DocumentChunk::getContent)
                .collect(Collectors.joining("\n\n"));

        // 4. Build prompt
        String prompt = "You are a helpful assistant. Answer the user's question using ONLY the context below. " +
                "If the answer is not in the context, say 'I don't know based on the provided documents.'\n\n" +
                "Context:\n" + context + "\n\n" +
                "Question: " + question;

        // 5. Get AI response
        AiMessage response = chatModel.generate(UserMessage.from(prompt)).content();
        return response.text();
    }
}

