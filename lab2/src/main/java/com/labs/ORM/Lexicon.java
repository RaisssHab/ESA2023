package com.labs.ORM;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Lexicon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lexicon_id")
    private Long lexiconId;

    @ManyToOne
    @JoinColumn(name = "semantic_id")
    private Semantics semantics;

    @ManyToOne
    @JoinColumn(name = "phrase_id")
    private Phrase phrase;

    private String text;

    // Constructors
    public Lexicon() {
    }

    public Lexicon(Semantics semantics, Phrase phrase, String text) {
        this.semantics = semantics;
        this.phrase = phrase;
        this.text = text;
    }

    // Getters and setters
    public Long getLexiconId() {
        return lexiconId;
    }

    public void setLexiconId(Long lexiconId) {
        this.lexiconId = lexiconId;
    }

    public Semantics getSemantics() {
        return semantics;
    }

    public void setSemantics(Semantics semantics) {
        this.semantics = semantics;
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(Phrase phrase) {
        this.phrase = phrase;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
