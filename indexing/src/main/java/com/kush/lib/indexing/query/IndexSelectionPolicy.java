package com.kush.lib.indexing.query;

import java.util.Iterator;

import com.kush.lib.indexing.Indexes.IndexOption;

public interface IndexSelectionPolicy<T> {

    IndexResponse<T> execute(IndexQuery query, Iterator<IndexOption<T>> indexOptions);
}
