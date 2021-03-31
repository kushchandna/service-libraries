package com.kush.lib.indexing.query;

import com.kush.lib.indexing.Index;

public interface IndexOption<T> {

    String getIndexName();

    Object[] getIndexedFields();

    Index<?, T> getIndex();
}
