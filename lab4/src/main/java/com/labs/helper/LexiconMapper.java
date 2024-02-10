package com.labs.helper;

import java.util.HashMap;
import java.util.Map;

import com.labs.orm.Lexicon;
import com.labs.orm.Phrase;
import com.labs.orm.Semantics;

public class LexiconMapper {
    public static Map<String, Object> map(Lexicon lexicon, Phrase phrase, Semantics semantics) {
        Map<String, Object> map = new HashMap<>();
        map.put("lexiconId", lexicon.getLexiconId());
        map.put("text", lexicon.getText());
        map.put("phraseId", phrase.getPhraseId());
        map.put("semanticId", semantics.getSemanticId());
        return map;
    }
}
