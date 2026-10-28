package com.smartdocs.smartdocs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {

    // We cast the string parameter to vector in SQL
    @Query(value = "SELECT * FROM document_chunks ORDER BY embedding <=> cast(:embedding as vector) LIMIT 3", nativeQuery = true)
    List<DocumentChunk> findTop3Similar(@Param("embedding") String embeddingString);
}