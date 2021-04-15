package com.kush.lib.indexing.query.policies;

import java.util.Optional;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.collections.ranges.RangeSet;
import com.kush.lib.indexing.Index;
import com.kush.lib.indexing.query.IndexQuery;
import com.kush.lib.indexing.query.IndexSelectionPolicy;
import com.kush.lib.indexing.ranges.IndexableRangeSetFinder;

public abstract class BaseIndexSelectionPolicy<T> implements IndexSelectionPolicy<T> {

    protected final <K> Optional<IterableResult<T>> getResultFromIndex(Index<K, T> index, IndexQuery query, Object[] fields,
            IndexableRangeSetFinder<K> rangeSetFinder) {
        if (query.hasRangeDefinedFor(fields[0])) {
            Optional<RangeSet<K>> rangeSet = rangeSetFinder.find(fields, query);
            return rangeSet.map(index::getMatches);
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    protected final <K> Index<K, T> adapt(Index<?, T> index) {
        return (Index<K, T>) index;
    }
}
