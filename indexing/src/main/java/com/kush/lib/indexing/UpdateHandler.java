package com.kush.lib.indexing;

public interface UpdateHandler<T> {

    void onUpdate(T oldObject, T newObject);

    default void onAdd(T object) {
        onUpdate(null, object);
    }

    default void onRemove(T object) {
        onUpdate(object, null);
    }
}
