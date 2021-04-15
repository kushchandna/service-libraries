package com.kush.lib.indexing.composite;

import static com.kush.lib.collections.utils.MapUtils.getAllCombinations;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.kush.lib.collections.ranges.RangeOperator;
import com.kush.lib.collections.ranges.RangeSet;
import com.kush.lib.indexing.query.IndexQuery;
import com.kush.lib.indexing.ranges.IndexableRangeSetFinder;

public class CompositeKeyRangeSetFinder implements IndexableRangeSetFinder<CompositeKey> {

    private static Comparator<Object> DUMMY_COMPARATOR = (o1, o2) -> 0;

    @Override
    public Optional<RangeSet<CompositeKey>> find(Object[] fields, IndexQuery indexQuery) {
        Map<Object, List<RangeSet<Object>>> fieldRanges = getFieldRanges(fields, indexQuery);
        Collection<Map<Object, RangeSet<Object>>> allCombinations = getAllCombinations(fieldRanges);
        for (Map<Object, RangeSet<Object>> fieldVsRange : allCombinations) {
            replaceRangeSetsSucceedingNonPointOnesWithUnboundedOnes(fields, fieldVsRange);
        }
        fieldRanges.toString();
        return null;
    }

    private Map<Object, List<RangeSet<Object>>> getFieldRanges(Object[] fields, IndexQuery indexQuery) {
        Map<Object, List<RangeSet<Object>>> fieldRanges = new HashMap<>();
        for (Object field : fields) {
            Optional<RangeSet<Object>> ranges = indexQuery.getRanges(field);
            RangeSet<Object> rangeSet = ranges.orElseGet(() -> universal());
            fieldRanges.put(field, rangeSet.split());
        }
        return fieldRanges;
    }

    private void replaceRangeSetsSucceedingNonPointOnesWithUnboundedOnes(Object[] fields,
            Map<Object, RangeSet<Object>> fieldRanges) {

        boolean allPrecidingContainsOnlyPointRanges = true;
        for (Object field : fields) {

            RangeSet<Object> individualRangeSet = fieldRanges.get(field);
            // it is guaranteed that (individualRangeSet != null) here

            if (!allPrecidingContainsOnlyPointRanges) {
                // replace existing range set with an unbounded one
                fieldRanges.put(field, universal());
            }

            if (allPrecidingContainsOnlyPointRanges && !individualRangeSet.containsAllPointRanges()) {
                // mark that all subsequent range sets will be replaced with unbounded ones
                allPrecidingContainsOnlyPointRanges = false;
            }
        }
    }

    private RangeSet<Object> universal() {
        return RangeSet.universal(dummyRangeOperator());
    }

    private RangeOperator<Object> dummyRangeOperator() {
        return new RangeOperator<>(DUMMY_COMPARATOR, true);
    }
}
