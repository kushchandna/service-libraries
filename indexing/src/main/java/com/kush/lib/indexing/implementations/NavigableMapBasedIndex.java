package com.kush.lib.indexing.implementations;

import static java.util.Collections.emptySet;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

import javax.annotation.concurrent.NotThreadSafe;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.collections.ranges.Range;
import com.kush.lib.indexing.Index;
import com.kush.lib.indexing.UpdateHandler;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

@NotThreadSafe
public class NavigableMapBasedIndex<K, T> implements Index<K, T>, UpdateHandler<T>, Cloneable {

    private final Comparator<K> comparator;
    private final Function<T, K> keyGetter;

    private final NavigableMap<K, Collection<T>> indexedValues;

    public NavigableMapBasedIndex(Comparator<K> comparator, Function<T, K> keyGetter) {
        this(comparator, keyGetter, new TreeMap<>(comparator));
    }

    private NavigableMapBasedIndex(Comparator<K> comparator, Function<T, K> keyGetter,
            NavigableMap<K, Collection<T>> indexedValues) {
        this.comparator = comparator;
        this.keyGetter = keyGetter;
        this.indexedValues = indexedValues;
    }

    @Override
    public IterableResult<T> getMatchesForKey(K key) {
        Collection<T> matchingObjects = indexedValues.getOrDefault(key, emptySet());
        return IterableResult.fromCollection(matchingObjects);
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
    public NavigableMapBasedIndex<K, T> clone() {
        NavigableMap<K, Collection<T>> indexedValuesClone = cloneIndexedValues();
        return new NavigableMapBasedIndex<>(comparator, keyGetter, indexedValuesClone);
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
        indexedValues.computeIfAbsent(key, k -> new ObjectOpenHashSet<>()).add(object);
    }

    private void removeFromIndexedValues(K key, T object) {
        indexedValues.computeIfPresent(key, (k, objects) -> {
            objects.remove(object);
            return objects.isEmpty() ? null : objects;
        });
    }

    private NavigableMap<K, Collection<T>> cloneIndexedValues() {
        // create a copy of tree map in linear time using constructor accepting sorted map
        NavigableMap<K, Collection<T>> indexedValuesClone = new TreeMap<>(this.indexedValues);
        // iterate over all the nodes in clone in linear time and replace value collection with a copy
        indexedValuesClone.entrySet().forEach(this::replaceValueWithCopyCollection);
        return indexedValuesClone;
    }

    private Collection<T> replaceValueWithCopyCollection(Entry<K, Collection<T>> entry) {
        Collection<T> objects = entry.getValue();
        return entry.setValue(((ObjectOpenHashSet<T>) objects).clone());
    }
}
