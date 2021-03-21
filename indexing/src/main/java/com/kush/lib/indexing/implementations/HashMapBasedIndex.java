package com.kush.lib.indexing.implementations;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.collections.ranges.Range;
import com.kush.lib.collections.ranges.RangeOperator;
import com.kush.lib.indexing.Index;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class HashMapBasedIndex<K, T> implements Index<K, T> {

    private final Comparator<K> comparator;

    private final Map<K, Collection<T>> indexedValues;
    private final boolean isNullHigh;

    public HashMapBasedIndex(Comparator<K> comparator, boolean isNullHigh) {
        this.comparator = comparator;
        this.isNullHigh = isNullHigh;
        indexedValues = new Object2ObjectOpenHashMap<>();
    }

    @Override
    public IterableResult<T> getMatchesForKey(K key) {
        Collection<T> matches = indexedValues.getOrDefault(key, emptySet());
        return IterableResult.fromCollection(matches);
    }

    @Override
    public IterableResult<T> getMatchesForKeys(Collection<K> keys) {
        List<IterableResult<T>> results = new LinkedList<>();
        keys.forEach(key -> {
            IterableResult<T> matches = getMatchesForKey(key);
            if (matches != null) {
                results.add(matches);
            }
        });
        return IterableResult.merge(results);
    }

    @Override
    public IterableResult<T> getMatchesForRange(Range<K> range) {
        RangeOperator<K> rangeOperator = new RangeOperator<>(comparator, isNullHigh);
        List<Collection<T>> matchingLists = indexedValues.entrySet()
            .stream()
            .filter(entry -> rangeOperator.isInRange(range, entry.getKey()))
            .map(entry -> entry.getValue())
            .collect(toList());
        return IterableResult.fromCollections(matchingLists);
    }
}
