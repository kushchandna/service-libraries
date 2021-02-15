package com.kush.lib.indexing.implementations;

import static java.util.Comparator.naturalOrder;

import java.util.function.Function;

import com.kush.lib.indexing.CompositeIndex;
import com.kush.lib.indexing.CompositeIndex.MultiKey;

public class NavigableMapBasedCompositeIndex<T> extends NavigableMapBasedIndex<MultiKey, T> implements CompositeIndex<T> {

    public NavigableMapBasedCompositeIndex(Function<T, MultiKey> keyGetter) {
        super(naturalOrder(), keyGetter);
    }
}
