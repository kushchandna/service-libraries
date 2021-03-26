package com.kush.lib.collections.ranges;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.kush.lib.collections.ranges.Range.Builder;

/*
 * [1 - 2]
 * [* - 2]
 */
class DefaultRangeParser implements RangeParser {

    @Override
    public <T> Range<T> parse(String text, Function<String, T> valueParser) {
        if (!text.startsWith("[") && !text.startsWith("(")) {
            throw new IllegalArgumentException("Text representing range should start with either a '[' or '('");
        }
        if (!text.endsWith("]") && !text.endsWith(")")) {
            throw new IllegalArgumentException("Text representing range should end with either a ']' or ')'");
        }
        boolean isStartInclusive = text.startsWith("[");
        boolean isEndInclusive = text.endsWith("]");
        text = text.substring(1, text.length() - 1);
        String[] textStartEnd = text.split(" - ");

        Builder<T> rangeBuilder = Range.<T>builder();
        rangeBuilder = append(rangeBuilder, textStartEnd[0], valueParser,
                (builder, value) -> builder.startingFrom(value, isStartInclusive));
        rangeBuilder = append(rangeBuilder, textStartEnd[1], valueParser,
                (builder, value) -> builder.endingAt(value, isEndInclusive));
        return rangeBuilder.build();
    }

    private <T> Range.Builder<T> append(Range.Builder<T> builder, String text, Function<String, T> valueParser,
            BiFunction<Range.Builder<T>, T, Range.Builder<T>> builderFunction) {
        String valueText = text.trim();
        if (!"*".equals(valueText)) {
            return builderFunction.apply(builder, valueParser.apply(valueText));
        }
        return builder;
    }
}
