package com.kush.lib.indexing.ranges;

import java.util.Optional;

import com.kush.lib.collections.ranges.RangeSet;
import com.kush.lib.indexing.query.IndexQuery;

public interface IndexableRangeSetFinder<T> {

    Optional<RangeSet<T>> find(Object[] fields, IndexQuery indexQuery);

    static <T> Optional<RangeSet<T>> findForField(Object field, IndexQuery indexQuery) {
        return indexQuery.getRanges(field);
    }
}
