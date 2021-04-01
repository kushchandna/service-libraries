package com.kush.lib.indexing.management;

import com.kush.lib.indexing.Index;
import com.kush.lib.indexing.UpdateHandler;
import com.kush.lib.indexing.factory.IndexFactory;

public interface IndexGenerator<K, T> {

    <I extends Index<K, T> & UpdateHandler<T>> I generate(IndexFactory<T> indexFactory);
}
