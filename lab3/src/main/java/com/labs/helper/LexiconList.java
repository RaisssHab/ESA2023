package com.labs.helper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.labs.orm.Lexicon;

@XmlRootElement
public class LexiconList {
    private List<Lexicon> lexicon;

    @XmlElement
    public List<Lexicon> getLexicon() {
        return lexicon;
    }

    public void setLexicon(List<Lexicon> lexicons) {
        this.lexicon = lexicons;
    }
}
