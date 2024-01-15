package com.labs.orm;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "semantics")
@XmlAccessorType(XmlAccessType.FIELD)
public class Semantics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semantic_id")
    @JsonbProperty("semanticId")
    private Long semanticId;

    @JsonbProperty("translation")
    private String translation;
    @JsonbProperty("explanation")
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
