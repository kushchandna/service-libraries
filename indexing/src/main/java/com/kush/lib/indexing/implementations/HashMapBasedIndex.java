package com.kush.lib.indexing.implementations;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.collections.ranges.RangeSet;
import com.kush.lib.indexing.Index;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class HashMapBasedIndex<K, T> implements Index<K, T> {

    private final Map<K, Collection<T>> indexedValues;

    public HashMapBasedIndex() {
        indexedValues = new Object2ObjectOpenHashMap<>();
    }

    @Override
    public IterableResult<T> getMatches(RangeSet<K> rangeSet) {
        List<Collection<T>> matchingLists = indexedValues.entrySet()
            .stream()
            .filter(entry -> rangeSet.isInRangeSet(entry.getKey()))
            .map(entry -> entry.getValue())
            .collect(toList());
        return IterableResult.fromCollections(matchingLists);
    }
}
