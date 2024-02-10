package com.labs.orm;
import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "phrases")
@XmlRootElement(name = "phrase")
@XmlAccessorType(XmlAccessType.FIELD)
public class Phrase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phrase_id")
    @JsonbProperty("phraseId")
    private Long phraseId;

    @JsonbProperty("phrase")
    private String phrase;

    // Constructors
    public Phrase() {
    }

    public Phrase(String phrase) {
        this.phrase = phrase;
    }

    // Getters and setters
    public Long getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(Long phraseId) {
        this.phraseId = phraseId;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
}
