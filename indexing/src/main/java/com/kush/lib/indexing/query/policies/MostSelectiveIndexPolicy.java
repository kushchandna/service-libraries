package com.kush.lib.indexing.query.policies;

import java.util.Iterator;
import java.util.Optional;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.indexing.query.IndexOption;
import com.kush.lib.indexing.query.IndexQuery;
import com.kush.lib.indexing.query.IndexResponse;
import com.kush.lib.indexing.ranges.IndexableRangeSetFinder;

public class MostSelectiveIndexPolicy<T> extends BaseIndexSelectionPolicy<T> {

    @Override
    public <K> IndexResponse<T> execute(IndexQuery query, Iterator<IndexOption<T>> indexOptions,
            IndexableRangeSetFinder<K> rangeSetFinder) {
        Optional<IterableResult<T>> mostSelectiveResult = null;
        while (indexOptions.hasNext()) {
            IndexOption<T> indexOption = indexOptions.next();
            Object[] fields = indexOption.getIndexedFields();
            Optional<IterableResult<T>> result = getResultFromIndex(adapt(indexOption.getIndex()), query, fields, rangeSetFinder);
            if (result.isPresent()
                    && (!mostSelectiveResult.isPresent() || result.get().count() < mostSelectiveResult.get().count())) {
                mostSelectiveResult = result;
            }
        }
        return IndexResponse.with(mostSelectiveResult);
    }
}
