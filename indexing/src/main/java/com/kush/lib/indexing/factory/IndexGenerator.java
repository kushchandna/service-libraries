package com.kush.lib.indexing.factory;

import com.kush.lib.indexing.Index;
import com.kush.lib.indexing.UpdateHandler;

public interface IndexGenerator<T> {

    <I extends Index<?, T> & UpdateHandler<T>> I generate(IndexFactory<T> indexFactory);
}
