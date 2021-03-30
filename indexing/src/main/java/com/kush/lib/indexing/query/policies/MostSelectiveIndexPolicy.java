package com.kush.lib.indexing.query.policies;

import java.util.Iterator;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.indexing.Index;
import com.kush.lib.indexing.Indexes.IndexOption;
import com.kush.lib.indexing.MultiKey;
import com.kush.lib.indexing.query.IndexQuery;
import com.kush.lib.indexing.query.IndexResponse;

public class MostSelectiveIndexPolicy<T> extends BaseIndexSelectionPolicy<T> {

    @Override
    public <K> IndexResponse<T> execute(IndexQuery query, Iterator<IndexOption<K, T>> indexOptions,
            MultiKey.Factory multiKeyFactory) {
        IterableResult<T> mostSelectiveResult = null;
        while (indexOptions.hasNext()) {
            IndexOption<K, T> indexOption = indexOptions.next();
            Index<K, T> index = indexOption.getIndex();
            Object[] fields = indexOption.getIndexedFields();
            IterableResult<T> result = getResultFromIndex(index, query, fields).orElse(null);
            if (result != null && (mostSelectiveResult == null || result.count() < mostSelectiveResult.count())) {
                mostSelectiveResult = result;
            }
        }
        return IndexResponse.with(mostSelectiveResult);
    }
}
