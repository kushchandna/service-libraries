package com.kush.lib.indexing.factory;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;

import javax.annotation.concurrent.NotThreadSafe;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.collections.ranges.Range;
import com.kush.lib.collections.ranges.RangeSet;
import com.kush.lib.collections.utils.NullableOptional;
import com.kush.lib.indexing.UpdateHandler;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

@NotThreadSafe
class SortedKeyBasedIndex<K, T> extends BaseIndex<K, T> implements UpdateHandler<T>, Cloneable {

    private final Comparator<K> comparator;
    private final NavigableMap<K, Collection<T>> indexedValues;

    public SortedKeyBasedIndex(Comparator<K> comparator, Function<T, K> keyGetter) {
        this(comparator, keyGetter, new TreeMap<>(comparator));
    }

    private SortedKeyBasedIndex(Comparator<K> comparator, Function<T, K> keyGetter,
            NavigableMap<K, Collection<T>> indexedValues) {
        super(keyGetter);
        this.comparator = comparator;
        this.indexedValues = indexedValues;
    }

    @Override
    public IterableResult<T> getMatches(RangeSet<K> rangeSet) {
        return IterableResult.merge(rangeSet.getRanges().stream()
            .map(this::getMatchForRange)
            .filter(Objects::nonNull)
            .collect(toList()));
    }

    @Override
    protected void addToIndexedValues(K key, T object) {
        indexedValues.computeIfAbsent(key, k -> new ObjectOpenHashSet<>()).add(object);
    }

    @Override
    protected void removeFromIndexedValues(K key, T object) {
        indexedValues.computeIfPresent(key, (k, objects) -> {
            objects.remove(object);
            return objects.isEmpty() ? null : objects;
        });
    }

    @Override
    protected boolean areKeysEqual(K key1, K key2) {
        return comparator.compare(key1, key2) == 0;
    }

    @Override
    public SortedKeyBasedIndex<K, T> clone() {
        NavigableMap<K, Collection<T>> indexedValuesClone = cloneIndexedValues();
        return new SortedKeyBasedIndex<>(comparator, getKeyGetter(), indexedValuesClone);
    }

    private IterableResult<T> getMatchForRange(Range<K> range) {
        NullableOptional<K> start = range.getStart();
        NullableOptional<K> end = range.getEnd();
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
