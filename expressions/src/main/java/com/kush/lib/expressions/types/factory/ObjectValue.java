package com.kush.lib.expressions.types.factory;

import com.kush.lib.expressions.types.ComparableObject;
import com.kush.lib.expressions.types.Type;

final class ObjectValue<T extends Comparable<T>> extends BaseTypedValue {

    private final ComparableObject value;

    public ObjectValue(ComparableObject value) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.value = value;
    }

    @Override
    public ComparableObject getObject() {
        return value;
    }

    @Override
    public Type getType() {
        return Type.OBJECT;
    }

    @Override
    protected boolean nonNullValueEquals(BaseTypedValue other) {
        return value.equals(other.getObject());
    }

    @Override
    protected int nonNullValueHashCode() {
        return value.hashCode();
    }
}
