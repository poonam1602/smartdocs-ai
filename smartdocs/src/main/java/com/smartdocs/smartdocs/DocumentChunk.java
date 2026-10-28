package com.smartdocs.smartdocs;

import jakarta.persistence.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "document_chunks")
public class DocumentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String content;

    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 3072)
    @Column(columnDefinition = "vector(3072)")
    private float[] embedding;

    public DocumentChunk() {}

    public DocumentChunk(String content, float[] embedding) {
        this.content = content;
        this.embedding = embedding;
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public float[] getEmbedding() { return embedding; }
}