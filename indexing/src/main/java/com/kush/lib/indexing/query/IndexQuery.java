package com.kush.lib.indexing.query;

import java.util.Optional;

import com.kush.lib.collections.ranges.RangeSet;

public interface IndexQuery {

    Optional<RangeSet<Object>> getRanges(Object field);
}
