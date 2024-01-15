package com.labs.helper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.labs.orm.Phrase;

@XmlRootElement
public class PhraseList {
    private List<Phrase> phrase;

    @XmlElement
    public List<Phrase> getPhrase() {
        return phrase;
    }

    public void setPhrase(List<Phrase> phrases) {
        this.phrase = phrases;
    }
}
