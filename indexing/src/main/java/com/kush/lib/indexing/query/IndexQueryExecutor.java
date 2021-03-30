package com.kush.lib.indexing.query;

public interface IndexQueryExecutor<T> {

    IndexResponse<T> execute(IndexQuery query, IndexSelectionPolicy<T> policy);
}
