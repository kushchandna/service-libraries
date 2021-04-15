package com.kush.lib.indexing.composite;

public interface CompositeKey extends Comparable<CompositeKey> {

    Object[] getValues();
}
