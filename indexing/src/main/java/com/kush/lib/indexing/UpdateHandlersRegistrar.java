package com.kush.lib.indexing;

public interface UpdateHandlersRegistrar<T> {

    void register(UpdateHandler<T> updateHandler);

    void unregister(UpdateHandler<T> updateHandler);
}
