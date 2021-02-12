package com.kush.lib.expressions.types.factory;

import com.kush.lib.expressions.types.Type;

final class ByteValue extends BaseTypedValue {

    private final byte value;

    public ByteValue(byte value) {
        this.value = value;
    }

    @Override
    public byte getByte() {
        return value;
    }

    @Override
    public Object getObject() {
        return Byte.valueOf(value);
    }

    @Override
    public Type getType() {
        return Type.BYTE;
    }

    @Override
    protected boolean nonNullValueEquals(BaseTypedValue other) {
        return value == other.getByte();
    }

    @Override
    protected int nonNullValueHashCode() {
        return Byte.hashCode(value);
    }
}
