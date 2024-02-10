package com.labs.helper;

import java.util.HashMap;
import java.util.Map;

import com.labs.orm.Phrase;

public class PhraseMapper {
    public static Map<String, Object> map(Phrase phrase) {
        Map<String, Object> map = new HashMap<>();
        map.put("phraseId", phrase.getPhraseId());
        map.put("phrase", phrase.getPhrase());
        return map;
    }
}
