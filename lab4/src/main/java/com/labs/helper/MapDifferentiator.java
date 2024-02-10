package com.labs.helper;

import java.util.HashMap;
import java.util.Map;

public class MapDifferentiator {
    public static Map<String, Object> differentiate(Map<String, Object> map1, Map<String, Object> map2) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : map2.entrySet()) {
            String key = entry.getKey();
            Object value2 = entry.getValue();
            Object value1 = map1.get(key);

            if (value2 == null && value1 == null) {
                continue;
            } else if (value2 == null && value1 != null) {
                result.put(key, value2);
                continue;
            }

            if (!map1.containsKey(key) || !value2.equals(value1)) {
                result.put(key, value2);
            }
        }
        return result;
    }
}
