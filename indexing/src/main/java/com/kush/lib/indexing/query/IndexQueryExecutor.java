package com.kush.lib.indexing.query;

import java.util.Iterator;

import com.kush.lib.indexing.Indexes.IndexOption;

public interface IndexQueryExecutor<T> {

    IndexResponse<T> execute(IndexQuery query, IndexSelectionPolicy<T> policy);

    interface IndexSelectionPolicy<T> {

        IndexResponse<T> execute(IndexQuery query, Iterator<IndexOption<T>> indexOptions);
    }
}
