package com.kush.lib.indexing.factory;

import static java.util.Comparator.naturalOrder;

import java.util.Comparator;
import java.util.function.Function;

import com.kush.lib.indexing.Index;
import com.kush.lib.indexing.MultiKey;

public class IndexFactory<T> {

    public <K> Index<K, T> createIndexWithSortedKeys(Function<T, K> keyGetter, Comparator<K> comparator) {
        return new SortedKeyBasedIndex<>(comparator, keyGetter);
    }

    public <K extends Comparable<K>> Index<K, T> createIndexWithSortedKeys(Function<T, K> keyGetter) {
        return new SortedKeyBasedIndex<>(naturalOrder(), keyGetter);
    }

    public <K> Index<K, T> createIndexWithHashedKeys(Function<T, K> keyGetter) {
        return new HashBasedIndex<>(keyGetter);
    }

    public Index<MultiKey, T> composite(Index<MultiKey, T> index) {
        return new CompositeKeyIndex<>(index);
    }
}
