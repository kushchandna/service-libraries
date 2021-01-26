package com.kush.lib.indexing;

import java.util.Collection;

import com.kush.utils.commons.IterableResult;
import com.kush.utils.commons.Range;

public interface Index<K, T> {

    IterableResult<T> getMatchesForKey(K key);

    IterableResult<T> getMatchesForKeys(Collection<K> keys);

    IterableResult<T> getMatchesForRange(Range<K> range);
}
