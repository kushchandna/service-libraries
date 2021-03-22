package com.kush.lib.collections.ranges;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.kush.lib.collections.ranges.Range.Builder;
import com.kush.lib.collections.utils.NullableOptional;

public class RangeSet<T> {

    private final List<Range<T>> ranges;

    private final RangeOperator<T> rangeOperator;
    private final boolean isInvalid;

    public static <T> RangeSet<T> invalid(RangeOperator<T> rangeOperator) {
        return new RangeSet<>(rangeOperator, true, emptyList());
    }

    public static <T> RangeSet<T> withRange(RangeOperator<T> rangeOperator, Range<T> range) {
        if (rangeOperator.isEmpty(range)) {
            throw new IllegalStateException();
        }
        return new RangeSet<>(rangeOperator, false, singletonList(range));
    }

    // assumes specified lists are valid
    private static <T> RangeSet<T> withRanges(RangeOperator<T> rangeOperator, List<Range<T>> ranges) {
        return new RangeSet<>(rangeOperator, false, ranges);
    }

    // assumes specified lists are valid
    private RangeSet(RangeOperator<T> rangeOperator, boolean isInvalid, List<Range<T>> ranges) {
        if (isInvalid != ranges.isEmpty()) {
            throw new IllegalStateException();
        }
        this.rangeOperator = rangeOperator;
        this.isInvalid = isInvalid;
        this.ranges = unmodifiableList(ranges);
    }

    public List<Range<T>> getRanges() {
        return unmodifiableList(ranges);
    }

    public RangeSet<T> union(RangeSet<T> rangeSet) {
        RangeSet<T> union = this;
        for (Range<T> range : rangeSet.getRanges()) {
            union = union.union(range);
        }
        return union;
    }

    public RangeSet<T> intersect(RangeSet<T> rangeSet) {
        RangeSet<T> intersection = this;
        for (Range<T> range : rangeSet.getRanges()) {
            intersection = intersection.intersect(range);
        }
        return intersection;
    }

    @Override
    public String toString() {
        return "RangeSet [ranges=" + ranges + ", isInvalid=" + isInvalid + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isInvalid ? 1231 : 1237);
        result = prime * result + rangeOperator.hashCode();
        result = prime * result + ranges.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RangeSet<?> other = (RangeSet<?>) obj;
        if (isInvalid != other.isInvalid) {
            return false;
        }
        if (!rangeOperator.equals(other.rangeOperator)) {
            return false;
        }
        if (!ranges.equals(other.ranges)) {
            return false;
        }
        return true;
    }

    private RangeSet<T> union(Range<T> range) {
        if (rangeOperator.isEmpty(range)) {
            return this;
        }
        if (range.isAll()) {
            return RangeSet.withRange(rangeOperator, Range.all());
        }
        if (isInvalid) {
            return RangeSet.withRange(rangeOperator, range);
        }

        NullableOptional<T> startingPoint = range.getStart();
        boolean startInclusive = range.isStartInclusive();

        int lastRangeIndexBeforeStart = -1;
        if (startingPoint.isPresent()) {
            for (int i = 0; i < ranges.size(); i++) {
                Range<T> existingRange = ranges.get(i);
                int startPos = rangeOperator.locate(existingRange, startingPoint.get());
                if (startPos > 0) {
                    lastRangeIndexBeforeStart = i;
                    continue;
                }
                if (startPos == 0) {
                    startingPoint = existingRange.getStart();
                    startInclusive = existingRange.isStartInclusive();
                }
                break;
            }
        }

        NullableOptional<T> endingPoint = range.getEnd();
        boolean endInclusive = range.isEndInclusive();

        int firstRangeIndexAfterEnd = ranges.size();
        if (endingPoint.isPresent()) {
            for (int i = ranges.size() - 1; i >= 0; i--) {
                Range<T> existingRange = ranges.get(i);
                int endPos = rangeOperator.locate(existingRange, endingPoint.get());
                if (endPos < 0) {
                    firstRangeIndexAfterEnd = i;
                    continue;
                }
                if (endPos == 0) {
                    endingPoint = existingRange.getEnd();
                    endInclusive = existingRange.isEndInclusive();
                }
                break;
            }
        }

        List<Range<T>> newRanges = new ArrayList<>(ranges.size() + 1);

        for (int i = 0; i <= lastRangeIndexBeforeStart; i++) {
            newRanges.add(ranges.get(i));
        }

        Builder<T> rangeBuilder = Range.<T>builder();
        if (startingPoint.isPresent()) {
            rangeBuilder = rangeBuilder.startingFrom(startingPoint.get(), startInclusive);
        }
        if (endingPoint.isPresent()) {
            rangeBuilder = rangeBuilder.endingAt(endingPoint.get(), endInclusive);
        }
        newRanges.add(rangeBuilder.build());

        for (int i = firstRangeIndexAfterEnd; i < ranges.size(); i++) {
            newRanges.add(ranges.get(i));
        }

        return RangeSet.withRanges(rangeOperator, newRanges);
    }

    private RangeSet<T> intersect(Range<T> range) {
        if (isInvalid) {
            return this;
        }
        if (range.isAll()) {
            return this;
        }
        if (rangeOperator.isEmpty(range)) {
            return invalid(rangeOperator);
        }

        List<Range<T>> ranges = new ArrayList<>(getRanges().size());
        for (Range<T> currentRange : getRanges()) {
            Optional<Range<T>> intersection = rangeOperator.intersect(currentRange, range);
            if (intersection.isPresent()) {
                ranges.add(intersection.get());
            }
        }

        return RangeSet.withRanges(rangeOperator, ranges);
    }
}
