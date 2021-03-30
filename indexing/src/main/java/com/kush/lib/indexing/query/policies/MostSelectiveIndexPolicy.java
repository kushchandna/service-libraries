package com.kush.lib.indexing.query.policies;

import java.util.Iterator;
import java.util.Optional;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.collections.ranges.RangeSet;
import com.kush.lib.indexing.Index;
import com.kush.lib.indexing.Indexes.IndexOption;
import com.kush.lib.indexing.query.IndexQuery;
import com.kush.lib.indexing.query.IndexQueryExecutor;
import com.kush.lib.indexing.query.IndexResponse;

public class MostSelectiveIndexPolicy<T> implements IndexQueryExecutor.IndexSelectionPolicy<T> {

    @Override
    public IndexResponse<T> execute(IndexQuery query, Iterator<IndexOption<T>> indexOptions) {
        IterableResult<T> mostSelectiveResult = null;
        while (indexOptions.hasNext()) {
            IndexOption<T> indexOption = indexOptions.next();
            Index<Object, T> index = indexOption.getIndex();
            Object[] fields = indexOption.getIndexedFields();
            if (fields.length == 1) {
                // single select
                Optional<RangeSet<Object>> ranges = query.getRanges(fields[0]);
                if (ranges.isPresent()) {
                    IterableResult<T> result = index.getMatches(ranges.get());
                    if (mostSelectiveResult == null || result.count() < mostSelectiveResult.count()) {
                        mostSelectiveResult = result;
                    }
                }
            } else {
                // TODO multi-select
                throw new UnsupportedOperationException();
            }
        }
        return IndexResponse.with(mostSelectiveResult);
    }
}
