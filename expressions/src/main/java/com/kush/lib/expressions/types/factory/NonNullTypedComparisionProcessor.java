package com.kush.lib.expressions.types.factory;

import com.kush.lib.expressions.types.TypedValue;
import com.kush.lib.expressions.types.processors.IntReturningTypeProcessor;

class NonNullTypedComparisionProcessor extends IntReturningTypeProcessor {

    private final TypedValue o1;
    private final TypedValue o2;

    public NonNullTypedComparisionProcessor(TypedValue o1, TypedValue o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    @Override
    protected int handleBoolean() {
        return Boolean.compare(o1.getBoolean(), o2.getBoolean());
    }

    @Override
    protected int handleByte() {
        return Byte.compare(o1.getByte(), o2.getByte());
    }

    @Override
    protected int handleChar() {
        return Character.compare(o1.getChar(), o2.getChar());
    }

    @Override
    protected int handleInt() {
        return Integer.compare(o1.getInt(), o2.getInt());
    }

    @Override
    protected int handleLong() {
        return Long.compare(o1.getLong(), o2.getLong());
    }

    @Override
    protected int handleFloat() {
        return Float.compare(o1.getFloat(), o2.getFloat());
    }

    @Override
    protected int handleDouble() {
        return Double.compare(o1.getDouble(), o2.getDouble());
    }

    @Override
    protected int handleString() {
        return o1.getString().compareTo(o1.getString());
    }

    @Override
    protected int handleObject() {
        throw new UnsupportedOperationException();
    }
}
