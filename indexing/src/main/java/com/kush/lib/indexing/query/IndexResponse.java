package com.kush.lib.indexing.query;

import java.util.Optional;

import com.kush.lib.collections.iterables.IterableResult;

public interface IndexResponse<T> {

    static <T> IndexResponse<T> empty() {
        return Optional::empty;
    }

    static <T> IndexResponse<T> with(IterableResult<T> result) {
        return () -> Optional.of(result);
    }

    static <T> IndexResponse<T> with(Optional<IterableResult<T>> result) {
        return result.isPresent() ? with(result.get()) : empty();
    }

    Optional<IterableResult<T>> getResult();
}
