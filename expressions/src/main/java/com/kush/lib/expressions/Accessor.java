package com.kush.lib.expressions;

public interface Accessor<T> {

    TypedResult access(T object);
}
