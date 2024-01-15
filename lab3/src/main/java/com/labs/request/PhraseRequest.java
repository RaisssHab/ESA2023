package com.labs.request;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "phraseRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhraseRequest {
    @JsonbProperty("phraseId")
    private Long phraseId;
    @JsonbProperty("phrase") 
    private String phrase;

    public PhraseRequest() {}

    public Long getPhraseId() {
        return phraseId;
    }
    public String getPhrase() {
        return phrase;
    }

    public void setPhraseId(Long phraseId) {
        this.phraseId = phraseId;
    }
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
}
