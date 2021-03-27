package com.kush.lib.indexing.factory;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.collections.ranges.RangeSet;
import com.kush.lib.indexing.Index;
import com.kush.lib.indexing.MultiKey;

class CompositeKeyIndex<T> implements Index<MultiKey, T> {

    private final Index<MultiKey, T> underlying;

    public CompositeKeyIndex(Index<MultiKey, T> underlying) {
        this.underlying = underlying;
    }

    @Override
    public IterableResult<T> getMatches(RangeSet<MultiKey> rangeSet) {
        return underlying.getMatches(rangeSet);
    }
}