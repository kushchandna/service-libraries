package com.kush.lib.expressions.types.factory;

import com.kush.lib.expressions.types.Type;

class StringValue extends BaseTypedValue {

    private final String value;

    public StringValue(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.value = value;
    }

    @Override
    public String getString() {
        return value;
    }

    @Override
    public Object getObject() {
        return value;
    }

    @Override
    public Type getType() {
        return Type.STRING;
    }

    @Override
    protected boolean nonNullValueEquals(BaseTypedValue other) {
        return value.equals(other.getString());
    }

    @Override
    protected int nonNullValueHashCode() {
        return value.hashCode();
    }
}
