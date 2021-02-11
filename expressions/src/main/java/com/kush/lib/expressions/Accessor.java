package com.kush.lib.expressions;

public interface Accessor<T> {

    TypedValue access(T object) throws AccessException;
}
