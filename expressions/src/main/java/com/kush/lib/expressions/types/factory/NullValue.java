package com.kush.lib.expressions.types.factory;

import com.kush.lib.expressions.types.Type;

class NullValue extends BaseTypedValue {

    private final Type type;

    public NullValue(Type type) {
        this.type = type;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public Object getObject() {
        return null;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    protected boolean nonNullValueEquals(BaseTypedValue other) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected int nonNullValueHashCode() {
        throw new UnsupportedOperationException();
    }
}
