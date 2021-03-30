package com.kush.lib.indexing;

public interface MultiKey extends Comparable<MultiKey> {

    Object[] getValues();

    interface Factory {

        MultiKey create(Object[] values);
    }
}
