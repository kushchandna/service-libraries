package com.kush.lib.indexing.query;

import java.util.Iterator;

import com.kush.lib.indexing.composite.MultiKeyRangeSetGenerator;

public interface IndexSelectionPolicy<T> {

    IndexResponse<T> execute(IndexQuery query, Iterator<IndexOption<T>> indexOptions,
            MultiKeyRangeSetGenerator rangeSetGenerator);
}
