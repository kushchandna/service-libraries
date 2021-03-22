package com.kush.lib.collections.ranges;

import static java.util.Comparator.naturalOrder;

public class RangeSets {

    private static final boolean DEFAULT_IS_NULL_HIGH = false;

    @SafeVarargs
    public static <T extends Comparable<T>> RangeSet<T> fromValues(T... values) {
        RangeOperator<T> rangeOperator = new RangeOperator<T>(naturalOrder(), DEFAULT_IS_NULL_HIGH);
        RangeSet<T> rangeSet = RangeSet.empty(rangeOperator);
        for (T value : values) {
            RangeSet<T> valueRangeSet = fromValue(rangeOperator, value);
            rangeSet = rangeSet.union(valueRangeSet);
        }
        return rangeSet;
    }

    @SafeVarargs
    public static <T extends Comparable<T>> RangeSet<T> fromRanges(Range<T>... ranges) {
        RangeOperator<T> rangeOperator = new RangeOperator<T>(naturalOrder(), DEFAULT_IS_NULL_HIGH);
        RangeSet<T> rangeSet = RangeSet.empty(rangeOperator);
        for (Range<T> range : ranges) {
            RangeSet<T> valueRangeSet = fromRange(rangeOperator, range);
            rangeSet = rangeSet.union(valueRangeSet);
        }
        return rangeSet;
    }

    private static <T extends Comparable<T>> RangeSet<T> fromValue(RangeOperator<T> rangeOperator, T value) {
        Range<T> range = Range.fromValue(value);
        return fromRange(rangeOperator, range);
    }

    private static <T extends Comparable<T>> RangeSet<T> fromRange(RangeOperator<T> rangeOperator, Range<T> range) {
        return RangeSet.withRange(rangeOperator, range);
    }
}
