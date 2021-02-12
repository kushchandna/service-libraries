package com.kush.lib.expressions.types.factory;

import java.util.Comparator;

import com.kush.lib.expressions.types.Type;

final class ObjectValue extends BaseTypedValue {

    private final Object value;
    private final Comparator<Object> comparator;

    public ObjectValue(Object value, Comparator<Object> comparator) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.value = value;
        this.comparator = comparator;
    }

    @Override
    public Object getObject() {
        return value;
    }

    @Override
    public Type getType() {
        return Type.OBJECT;
    }

    @Override
    protected int compareNonNullObjects(Object o1, Object o2) {
        return comparator.compare(o1, o2);
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
