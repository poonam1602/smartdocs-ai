package com.smartdocs.smartdocs;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PdfService {

    private final DocumentChunkRepository repository;
    private final EmbeddingModel embeddingModel;

    public PdfService(DocumentChunkRepository repository, EmbeddingModel embeddingModel) {
        this.repository = repository;
        this.embeddingModel = embeddingModel;
    }

    public void processPdf(MultipartFile file) throws Exception {
        // 1. Clear the database so old PDFs don't mix with new ones
        repository.deleteAll();

        // 2. Read PDF text
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();

        // 3. Split text into chunks
        String[] words = text.split("\\s+");
        List<String> chunks = new ArrayList<>();
        StringBuilder currentChunk = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            currentChunk.append(words[i]).append(" ");
            if ((i + 1) % 500 == 0) {
                chunks.add(currentChunk.toString());
                currentChunk.setLength(0);
            }
        }
        if (currentChunk.length() > 0) chunks.add(currentChunk.toString());

        // 4. Generate embeddings and save to DB
        for (String chunk : chunks) {
            Embedding embedding = embeddingModel.embed(chunk).content();
            DocumentChunk docChunk = new DocumentChunk(chunk, embedding.vector());
            repository.save(docChunk);
        }
    }
}