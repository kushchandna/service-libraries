package com.kush.lib.expressions;

public interface Accessor<T> {

    ExpressionResult access(T object);
}
