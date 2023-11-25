package com.labs.ORM;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Semantics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semantic_id")
    private Long semanticId;

    private String translation;
    private String explanation;

    // Constructors
    public Semantics() {
    }

    public Semantics(String translation, String explanation) {
        this.translation = translation;
        this.explanation = explanation;
    }

    // Getters and setters
    public Long getSemanticId() {
        return semanticId;
    }

    public void setSemanticId(Long semanticId) {
        this.semanticId = semanticId;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
