package com.kush.lib.expressions.aspect;

import java.util.Collection;
import java.util.Optional;

public interface Aspect<T> {

    Optional<Field<T>> getField(String fieldName);

    Collection<Field<T>> getFields();
}
