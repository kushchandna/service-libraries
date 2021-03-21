package com.kush.lib.collections.ranges;

import java.util.function.Function;

public interface RangeParser {

    <T> Range<T> parse(String text, Function<String, T> valueParser);

    static <T> RangeParser defaultParser() {
        return new DefaultRangeParser();
    }
}
