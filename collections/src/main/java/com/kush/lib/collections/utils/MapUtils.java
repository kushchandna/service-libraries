package com.kush.lib.collections.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapUtils {

    public static <K, V> Map<K, V> intersect(Map<K, V> map1, Map<K, V> map2) {
        KeyConflictHandler<K, V> conflictHandler = (key, val1, val2) -> val2;
        return intersect(map1, map2, conflictHandler);
    }

    public static <K, V> Map<K, V> intersect(Map<K, V> map1, Map<K, V> map2, KeyConflictHandler<K, V> conflictHandler) {
        Set<K> commonKeys = new HashSet<>(map1.keySet());
        commonKeys.retainAll(map2.keySet());

        Map<K, V> intersection = new HashMap<>();
        commonKeys.forEach(key -> {
            V value1 = map1.get(key);
            V value2 = map2.get(key);
            V finalValue = conflictHandler.getFinalValue(key, value1, value2);
            intersection.put(key, finalValue);
        });
        return intersection;
    }

    public static <K, V> Map<K, V> union(Map<K, V> map1, Map<K, V> map2) {
        KeyConflictHandler<K, V> matchHandler = (key, val1, val2) -> val2;
        return union(map1, map2, matchHandler);
    }

    public static <K, V> Map<K, V> union(Map<K, V> map1, Map<K, V> map2, KeyConflictHandler<K, V> conflictHandler) {
        Set<K> commonKeys = new HashSet<>(map1.keySet());
        commonKeys.addAll(map2.keySet());

        Map<K, V> union = new HashMap<>();
        commonKeys.forEach(key -> {

            V value1 = map1.get(key);
            V value2 = map2.get(key);

            V finalValue = null;
            if (map1.containsKey(key) && map2.containsKey(key)) {
                finalValue = conflictHandler.getFinalValue(key, value1, value2);
            } else if (map1.containsKey(key)) {
                finalValue = map1.get(key);
            } else if (map2.containsKey(key)) {
                finalValue = map2.get(key);
            } else {
                throw new IllegalStateException();
            }

            union.put(key, finalValue);
        });
        return union;
    }

    public static <K, V> Collection<Map<K, V>> getAllCombinations(Map<K, ? extends Collection<V>> possibleValues) {
        List<Map<K, V>> allCombinations = new ArrayList<>();
        List<K> keys = new ArrayList<>(possibleValues.keySet());
        Map<K, V> singleValueMap = new HashMap<>();
        allCombinations.add(singleValueMap);
        addCombinations(possibleValues, allCombinations, keys, 0, singleValueMap);
        return allCombinations;
    }

    private static <V, K> void addCombinations(Map<K, ? extends Collection<V>> possibleValues, List<Map<K, V>> allCombinations,
            List<K> keys, int start, Map<K, V> singleValueMap) {
        for (int i = start; i < keys.size(); i++) {
            K key = keys.get(i);
            Collection<V> values = possibleValues.get(key);
            if (values.size() == 1) {
                singleValueMap.put(key, values.iterator().next());
                continue;
            }
            for (V value : values) {
                Map<K, V> singleValueMapClone = new HashMap<>(singleValueMap);
                singleValueMapClone.put(key, value);
                allCombinations.add(singleValueMapClone);
                addCombinations(possibleValues, allCombinations, keys, i + 1, singleValueMapClone);
            }
            allCombinations.remove(singleValueMap);
            break;
        }
    }

    public interface KeyConflictHandler<K, V> {

        V getFinalValue(K matchingKey, V value1, V value2);
    }
}
