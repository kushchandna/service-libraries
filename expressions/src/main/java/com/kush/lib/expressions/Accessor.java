package com.kush.lib.expressions;

import com.kush.lib.expressions.types.TypedValue;

public interface Accessor<T> {

    TypedValue access(T object) throws AccessException;
}
