package com.kush.lib.indexing.query;

import java.util.Optional;

import com.kush.lib.collections.iterables.IterableResult;

public interface IndexResponse<T> {

    Optional<IterableResult<T>> getResult();
}
