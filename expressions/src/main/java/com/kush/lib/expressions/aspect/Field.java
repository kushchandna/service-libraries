package com.kush.lib.expressions.aspect;

import com.kush.lib.expressions.Accessor;
import com.kush.lib.expressions.Type;

public interface Field<T> {

    String getName();

    Type getType();

    Accessor<T> getAccessor();
}
