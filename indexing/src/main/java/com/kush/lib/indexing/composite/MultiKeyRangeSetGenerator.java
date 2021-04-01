package com.kush.lib.indexing.composite;

import com.kush.lib.collections.ranges.RangeSet;
import com.kush.lib.indexing.query.IndexQuery;

public interface MultiKeyRangeSetGenerator {

    RangeSet<MultiKey> generate(Object[] fields, IndexQuery indexQuery);
}
