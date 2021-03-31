package com.kush.lib.indexing.query;

import java.util.Iterator;

import com.kush.lib.indexing.Indexes.IndexOption;
import com.kush.lib.indexing.composite.MultiKey;

public interface IndexSelectionPolicy<T> {

    <K> IndexResponse<T> execute(IndexQuery query, Iterator<IndexOption<K, T>> indexOptions, MultiKey.Factory multiKeyFactory);
}
