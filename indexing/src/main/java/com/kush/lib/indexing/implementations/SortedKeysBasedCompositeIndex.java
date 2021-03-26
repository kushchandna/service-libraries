package com.kush.lib.indexing.implementations;

import static java.util.Comparator.naturalOrder;

import java.util.function.Function;

import com.kush.lib.indexing.CompositeIndex;
import com.kush.lib.indexing.CompositeIndex.MultiKey;

public class SortedKeysBasedCompositeIndex<T> extends SortedKeyBasedIndex<MultiKey, T> implements CompositeIndex<T> {

    public SortedKeysBasedCompositeIndex(Function<T, MultiKey> keyGetter) {
        super(naturalOrder(), keyGetter);
    }
}
