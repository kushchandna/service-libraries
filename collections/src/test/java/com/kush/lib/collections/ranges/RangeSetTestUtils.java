package com.kush.lib.collections.ranges;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class RangeSetTestUtils {

    @SuppressWarnings("unchecked")
    public static RangeSet<Integer> intRanges(String... rangeTexts) {
        if (rangeTexts.length == 0) {
            return RangeSets.empty();
        }
        return RangeSets.fromRanges(stream(rangeTexts)
            .map(RangeParser::parseIntRange)
            .toArray(Range[]::new));
    }

    public static <T> String asString(RangeSet<T> rangeSet) {
        return rangeSet.getRanges()
            .stream()
            .map(RangeParser::toString)
            .collect(joining(", "));
    }
}
