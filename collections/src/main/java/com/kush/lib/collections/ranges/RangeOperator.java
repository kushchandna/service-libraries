package com.kush.lib.collections.ranges;

import static java.util.Comparator.nullsFirst;
import static java.util.Comparator.nullsLast;

import java.util.Comparator;
import java.util.Optional;

public class RangeOperator<T> {

    private final Comparator<T> comparator;

    public RangeOperator(Comparator<T> comparator, boolean isNullHigh) {
        this.comparator = isNullHigh ? nullsLast(comparator) : nullsFirst(comparator);
    }

    public boolean isAll(Range<T> range) {
        return range.isAll();
    }

    public boolean isPointRange(Range<T> range) {
        return range.isPointRange();
    }

    public boolean isInRange(Range<T> range, T key) {
        if (range.getStart().isPresent()) {
            int resultStart = comparator.compare(key, range.getStart().get());
            if (resultStart < 0) {
                return false;
            }
            if (resultStart == 0 && !range.isStartInclusive()) {
                return false;
            }
        }
        if (range.getEnd().isPresent()) {
            int resultEnd = comparator.compare(key, range.getEnd().get());
            if (resultEnd < 0) {
                return false;
            }
            if (resultEnd == 0 && !range.isEndInclusive()) {
                return false;
            }
        }
        return true;
    }

    public Optional<Range<T>> intersect(Range<T> range1, Range<T> range2) {
        return null;
    }
}
