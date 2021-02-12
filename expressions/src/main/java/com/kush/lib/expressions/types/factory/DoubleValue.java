package com.kush.lib.expressions.types.factory;

import com.kush.lib.expressions.types.Type;

final class DoubleValue extends BaseTypedValue {

    private final double value;

    public DoubleValue(double value) {
        this.value = value;
    }

    @Override
    public double getDouble() {
        return value;
    }

    @Override
    public Object getObject() {
        return Double.valueOf(value);
    }

    @Override
    public Type getType() {
        return Type.DOUBLE;
    }

    @Override
    protected boolean nonNullValueEquals(BaseTypedValue other) {
        return value == other.getDouble();
    }

    @Override
    protected int nonNullValueHashCode() {
        return Double.hashCode(value);
    }
}
