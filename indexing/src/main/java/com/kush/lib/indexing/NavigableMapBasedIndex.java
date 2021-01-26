package com.kush.lib.indexing;

import static java.util.Collections.emptySet;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

import javax.annotation.concurrent.NotThreadSafe;

import com.kush.utils.commons.IterableResult;
import com.kush.utils.commons.Range;

@NotThreadSafe
public class NavigableMapBasedIndex<K, T> implements Index<K, T>, UpdateHandler<T> {

    private final Comparator<K> comparator;
    private final Function<T, K> keyGetter;

    private final TreeMap<K, Collection<T>> indexedValues;

    public NavigableMapBasedIndex(Comparator<K> comparator, Function<T, K> keyGetter) {
        this.comparator = comparator;
        this.keyGetter = keyGetter;
        indexedValues = new TreeMap<>(comparator);
    }

    @Override
    public IterableResult<T> getMatchesForKey(K key) {
        Collection<T> matchingObjects = indexedValues.getOrDefault(key, emptySet());
        return IterableResult.fromCollection(matchingObjects);
    }

    @Override
    public IterableResult<T> getMatchesForKeys(Collection<K> keys) {
        IterableResult<T> result = IterableResult.empty();
        for (K key : keys) {
            result = IterableResult.merge(result, getMatchesForKey(key));
        }
        return result;
    }

    @Override
    public IterableResult<T> getMatchesForRange(Range<K> range) {
        if (range == null) {
            throw new NullPointerException("range");
        }
        Optional<K> start = range.getStart();
        Optional<K> end = range.getEnd();
        NavigableMap<K, Collection<T>> resultMap;
        if (start.isPresent() && end.isPresent()) {
            resultMap = indexedValues.subMap(start.get(), range.isStartInclusive(), end.get(), range.isEndInclusive());
        } else if (start.isPresent()) {
            resultMap = indexedValues.tailMap(start.get(), range.isStartInclusive());
        } else if (end.isPresent()) {
            resultMap = indexedValues.headMap(end.get(), range.isEndInclusive());
        } else {
            resultMap = indexedValues;
        }
        return IterableResult.fromCollections(resultMap.values());
    }

    @Override
    public void onUpdate(T oldObject, T newObject) {
        K oldKey = oldObject == null ? null : keyGetter.apply(oldObject);
        K newKey = newObject == null ? null : keyGetter.apply(newObject);
        if (oldObject == newObject && comparator.compare(oldKey, newKey) == 0) {
            return;
        }
        if (oldObject != null) {
            removeFromIndexedValues(oldKey, oldObject);
        }
        if (newObject != null) {
            addToIndexedValues(newKey, newObject);
        }
    }

    private void addToIndexedValues(K key, T object) {
        indexedValues.computeIfAbsent(key, k -> new HashSet<>()).add(object);
    }

    private void removeFromIndexedValues(K key, T object) {
        indexedValues.computeIfPresent(key, (k, objects) -> {
            objects.remove(object);
            return objects.isEmpty() ? null : objects;
        });
    }
}
