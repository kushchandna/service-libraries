package com.kush.lib.indexing.factory;

import java.util.function.Function;

import com.kush.lib.indexing.Index;
import com.kush.lib.indexing.UpdateHandler;

abstract class BaseIndex<K, T> implements Index<K, T>, UpdateHandler<T> {

    private final Function<T, K> keyGetter;

    public BaseIndex(Function<T, K> keyGetter) {
        this.keyGetter = keyGetter;
    }

    @Override
    public void onUpdate(T oldObject, T newObject) {
        K oldKey = oldObject == null ? null : keyGetter.apply(oldObject);
        K newKey = newObject == null ? null : keyGetter.apply(newObject);
        if (oldObject == newObject && areKeysEqual(oldKey, newKey)) {
            return;
        }
        if (oldObject != null) {
            removeFromIndexedValues(oldKey, oldObject);
        }
        if (newObject != null) {
            addToIndexedValues(newKey, newObject);
        }
    }

    protected final K getValue(T object) {
        return keyGetter.apply(object);
    }

    protected final Function<T, K> getKeyGetter() {
        return keyGetter;
    }

    protected abstract void addToIndexedValues(K newKey, T newObject);

    protected abstract void removeFromIndexedValues(K oldKey, T oldObject);

    protected abstract boolean areKeysEqual(K oldKey, K newKey);
}
