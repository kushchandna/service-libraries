package com.kush.lib.indexing.query;

import java.util.Optional;

import com.kush.lib.collections.ranges.RangeSet;

public interface IndexQuery {

    <K> Optional<RangeSet<K>> getRanges(Object field);
}
