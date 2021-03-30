package com.kush.lib.indexing.query.policies;

import java.util.Optional;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.collections.ranges.RangeSet;
import com.kush.lib.indexing.Index;
import com.kush.lib.indexing.query.IndexQuery;
import com.kush.lib.indexing.query.IndexSelectionPolicy;

public abstract class BaseIndexSelectionPolicy<T> implements IndexSelectionPolicy<T> {

    protected final <K> Optional<IterableResult<T>> getResultFromIndex(Index<K, T> index, IndexQuery query, Object[] fields) {
        if (fields.length == 1) {
            return getResultFromSingleFieldIndex(index, query, fields[0]);
        } else {
            return getResultFromMultiFieldsIndex(index, query, fields);
        }
    }

    protected final <K> Optional<IterableResult<T>> getResultFromSingleFieldIndex(Index<K, T> index, IndexQuery query,
            Object field) {
        Optional<RangeSet<K>> ranges = query.getRanges(field);
        if (ranges.isPresent()) {
            IterableResult<T> matches = index.getMatches(ranges.get());
            return Optional.of(matches);
        }
        return Optional.empty();
    }

    protected final <K> Optional<IterableResult<T>> getResultFromMultiFieldsIndex(Index<K, T> index, IndexQuery query,
            Object[] fields) {
        throw new UnsupportedOperationException();
    }
}
