package com.labs.request;

import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "lexiconRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class LexiconRequest {
    @JsonbProperty("lexiconId")
    private Long lexiconId;
    @JsonbProperty("text") 
    private String text;
    @JsonbProperty("semanticId")
    private Long semanticId;
    @JsonbProperty("phraseId") 
    private Long phraseId;

    public LexiconRequest() {}

    public Long getLexiconId() {
        return lexiconId;
    }
    public String getText() {
        return text;
    }
    public Long getSemanticId() {
        return semanticId;
    }
    public Long getPhraseId() {
        return phraseId;
    }

    public void setLexiconId(Long lexiconId) {
        this.lexiconId = lexiconId;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setSemanticId(Long semanticId) {
        this.semanticId = semanticId;
    }
    public void setPhraseId(Long phraseId) {
        this.phraseId = phraseId;
    }
}
