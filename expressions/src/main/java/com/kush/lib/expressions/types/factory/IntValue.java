package com.kush.lib.expressions.types.factory;

import com.kush.lib.expressions.types.Type;

final class IntValue extends BaseTypedValue {

    private final int value;

    public IntValue(int value) {
        this.value = value;
    }

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public Object getObject() {
        return Integer.valueOf(value);
    }

    @Override
    public Type getType() {
        return Type.INTEGER;
    }

    @Override
    protected boolean nonNullValueEquals(BaseTypedValue other) {
        return value == other.getInt();
    }

    @Override
    protected int nonNullValueHashCode() {
        return Integer.hashCode(value);
    }
}
