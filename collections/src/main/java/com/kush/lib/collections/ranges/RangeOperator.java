package com.kush.lib.collections.ranges;

import static java.util.Comparator.nullsFirst;
import static java.util.Comparator.nullsLast;

import java.util.Comparator;

import com.kush.lib.collections.utils.NullableOptional;

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

    public boolean isEmpty(Range<T> range) {
        if (range.getStart().isAbsent() || range.getEnd().isAbsent()) {
            return false;
        }
        int comparision = comparator.compare(range.getStart().get(), range.getEnd().get());
        if (comparision == 0) {
            return range.isStartInclusive() && range.isEndInclusive();
        }
        return comparision > 0;
    }

    public boolean isInRange(Range<T> range, T key) {
        if (isEmpty(range)) {
            return false;
        }
        int startComparision = startComparator().compare(range.getStart(), NullableOptional.of(key));
        if (startComparision > 0) {
            return false;
        }
        if (startComparision == 0 && !range.isStartInclusive()) {
            return false;
        }
        int endComparision = endComparator().compare(NullableOptional.of(key), range.getEnd());
        if (endComparision > 0) {
            return false;
        }
        if (endComparision == 0 && !range.isEndInclusive()) {
            return false;
        }
        return true;
    }

    private Comparator<NullableOptional<T>> startComparator() {
        return new RangePointComparator<>(comparator, false);
    }

    private Comparator<NullableOptional<T>> endComparator() {
        return new RangePointComparator<>(comparator, true);
    }

    private static class RangePointComparator<T> implements Comparator<NullableOptional<T>> {

        private final Comparator<T> valueComparator;
        private final boolean isEmptyHigh;

        public RangePointComparator(Comparator<T> valueComparator, boolean isEmptyHigh) {
            this.valueComparator = valueComparator;
            this.isEmptyHigh = isEmptyHigh;
        }

        @Override
        public int compare(NullableOptional<T> o1, NullableOptional<T> o2) {
            if (o1.isAbsent() && o2.isAbsent()) {
                return 0;
            }
            if (o1.isAbsent()) {
                // isEmptyHigh true - o1 is larger than o2
                // isEmptyHigh false - o1 is smaller than o2
                return isEmptyHigh ? 1 : -1;
            }
            if (o2.isAbsent()) {
                // isEmptyHigh true - o2 is larger than o1
                // isEmptyHigh false - o2 is smaller than o1
                return isEmptyHigh ? -1 : 1;
            }
            // both o1 and o2 contain a value
            return valueComparator.compare(o1.get(), o2.get());
        }
    }
}
