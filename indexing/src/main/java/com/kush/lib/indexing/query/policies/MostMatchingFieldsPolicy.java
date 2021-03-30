package com.kush.lib.indexing.query.policies;

import java.util.Iterator;
import java.util.Optional;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.collections.ranges.RangeSet;
import com.kush.lib.indexing.Indexes.IndexOption;
import com.kush.lib.indexing.MultiKey;
import com.kush.lib.indexing.query.IndexQuery;
import com.kush.lib.indexing.query.IndexResponse;

public class MostMatchingFieldsPolicy<T> extends BaseIndexSelectionPolicy<T> {

    @Override
    public <K> IndexResponse<T> execute(IndexQuery query, Iterator<IndexOption<K, T>> indexOptions,
            MultiKey.Factory multiKeyFactory) {
        int maxMatchingFields = 0;
        IndexOption<K, T> bestOption = null;
        while (indexOptions.hasNext()) {
            IndexOption<K, T> indexOption = indexOptions.next();
            Object[] fields = indexOption.getIndexedFields();
            int matchingFields = 0;
            for (Object field : fields) {
                Optional<RangeSet<K>> ranges = query.getRanges(field);
                if (!ranges.isPresent() || !ranges.get().containsAllPointRanges()) {
                    break;
                }
                matchingFields++;
            }
            if (matchingFields > maxMatchingFields) {
                maxMatchingFields = matchingFields;
                bestOption = indexOption;
            }
        }
        if (bestOption == null) {
            return IndexResponse.empty();
        }
        Optional<IterableResult<T>> result = getResultFromIndex(bestOption.getIndex(), query, bestOption.getIndexedFields());
        return IndexResponse.with(result.get());
    }
}
