package com.kush.lib.collections.ranges;

import java.util.function.Function;

import com.kush.lib.collections.utils.NullableOptional;
import com.kush.lib.collections.utils.Serializer;

class DefaultRangeSerializer<T> implements Serializer<Range<T>> {

    @Override
    public String toString(Range<T> range) {
        return new StringBuilder()
            .append(range.isStartInclusive() ? "[" : "(")
            .append(getValue(range, Range::getStart))
            .append(" - ")
            .append(getValue(range, Range::getEnd))
            .append(range.isEndInclusive() ? "]" : ")")
            .toString();
    }

    private String getValue(Range<T> range, Function<Range<T>, NullableOptional<T>> valueGetter) {
        NullableOptional<T> value = valueGetter.apply(range);
        return value.isPresent() ? String.valueOf(value.get()) : "*";
    }
}
