package com.kush.lib.indexing.query;

import java.util.Iterator;

import com.kush.lib.indexing.composite.MultiKey;

public interface IndexSelectionPolicy<T> {

    IndexResponse<T> execute(IndexQuery query, Iterator<IndexOption<T>> indexOptions, MultiKey.Factory multiKeyFactory);
}
