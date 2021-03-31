package com.kush.lib.indexing.query.policies;

import java.util.Iterator;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.indexing.Index;
import com.kush.lib.indexing.composite.MultiKey;
import com.kush.lib.indexing.query.IndexOption;
import com.kush.lib.indexing.query.IndexQuery;
import com.kush.lib.indexing.query.IndexResponse;

public class MostSelectiveIndexPolicy<T> extends BaseIndexSelectionPolicy<T> {

    @Override
    public IndexResponse<T> execute(IndexQuery query, Iterator<IndexOption<T>> indexOptions, MultiKey.Factory multiKeyFactory) {
        IterableResult<T> mostSelectiveResult = null;
        while (indexOptions.hasNext()) {
            IndexOption<T> indexOption = indexOptions.next();
            Index<?, T> index = indexOption.getIndex();
            Object[] fields = indexOption.getIndexedFields();
            IterableResult<T> result = getResultFromIndex(index, query, fields).orElse(null);
            if (result != null && (mostSelectiveResult == null || result.count() < mostSelectiveResult.count())) {
                mostSelectiveResult = result;
            }
        }
        return IndexResponse.with(mostSelectiveResult);
    }
}
