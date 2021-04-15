package com.kush.lib.indexing.query;

import java.util.Iterator;

import com.kush.lib.indexing.ranges.IndexableRangeSetFinder;

public interface IndexSelectionPolicy<T> {

    <K> IndexResponse<T> execute(IndexQuery query, Iterator<IndexOption<T>> indexOptions,
            IndexableRangeSetFinder<K> rangeSetFinder);
}
