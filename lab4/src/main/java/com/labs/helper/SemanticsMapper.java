package com.labs.helper;

import java.util.HashMap;
import java.util.Map;

import com.labs.orm.Semantics;

public class SemanticsMapper {
    public static Map<String, Object> map(Semantics semantics) {
        Map<String, Object> map = new HashMap<>();
        map.put("semanticId", semantics.getSemanticId());
        map.put("translation", semantics.getTranslation());
        map.put("explanation", semantics.getExplanation());
        return map;
    }
}
