package com.kush.lib.expressions.types.factory;

import com.kush.lib.expressions.types.Type;

final class LongValue extends BaseTypedValue {

    private final long value;

    public LongValue(long value) {
        this.value = value;
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public Object getObject() {
        return Long.valueOf(value);
    }

    @Override
    public Type getType() {
        return Type.LONG;
    }

    @Override
    protected boolean nonNullValueEquals(BaseTypedValue other) {
        return value == other.getLong();
    }

    @Override
    protected int nonNullValueHashCode() {
        return Long.hashCode(value);
    }
}
