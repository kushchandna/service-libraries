package com.kush.lib.collections.ranges;

import java.util.function.Function;

public interface RangeParser {

    <T> Range<T> parse(String text, Function<String, T> valueParser);

    static <T> RangeParser defaultParser() {
        return new DefaultRangeParser();
    }

    static Range<Integer> parseIntRange(String text) {
        return defaultParser().parse(text, Integer::parseInt);
    }

    static Range<Byte> parseByteRange(String text) {
        return defaultParser().parse(text, Byte::parseByte);
    }

    static Range<Long> parseLongRange(String text) {
        return defaultParser().parse(text, Long::parseLong);
    }

    static Range<Float> parseFloatRange(String text) {
        return defaultParser().parse(text, Float::parseFloat);
    }

    static Range<Double> parseDoubleRange(String text) {
        return defaultParser().parse(text, Double::parseDouble);
    }

    static <T> String toString(Range<T> range) {
        return new DefaultRangeSerializer<T>().toString(range);
    }
}
