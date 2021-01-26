package com.kush.lib.indexing;

import com.kush.lib.indexing.CompositeIndex.MultiKey;

public interface CompositeIndex<T> extends Index<MultiKey, T> {

    public interface MultiKey extends Comparable<MultiKey> {

        Object[] getValues();
    }
}
