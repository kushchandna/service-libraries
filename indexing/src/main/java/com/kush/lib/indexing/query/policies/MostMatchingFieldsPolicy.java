package com.kush.lib.indexing.query.policies;

import java.util.Iterator;
import java.util.Optional;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.collections.ranges.RangeSet;
import com.kush.lib.indexing.query.IndexOption;
import com.kush.lib.indexing.query.IndexQuery;
import com.kush.lib.indexing.query.IndexResponse;
import com.kush.lib.indexing.ranges.IndexableRangeSetFinder;

public class MostMatchingFieldsPolicy<T> extends BaseIndexSelectionPolicy<T> {

    @Override
    public <K> IndexResponse<T> execute(IndexQuery query, Iterator<IndexOption<T>> indexOptions,
            IndexableRangeSetFinder<K> rangeSetFinder) {
        int maxMatchingFields = 0;
        IndexOption<T> bestOption = null;
        while (indexOptions.hasNext()) {
            IndexOption<T> indexOption = indexOptions.next();
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
        Optional<IterableResult<T>> result = getResultFromIndex(adapt(bestOption.getIndex()), query,
                bestOption.getIndexedFields(), rangeSetFinder);
        return IndexResponse.with(result.get());
    }
}
