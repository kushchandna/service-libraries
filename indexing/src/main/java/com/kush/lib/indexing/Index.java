package com.kush.lib.indexing;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.collections.ranges.RangeSet;

public interface Index<K, T> {

    IterableResult<T> getMatches(RangeSet<K> rangeSet);
}
