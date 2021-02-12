package com.kush.lib.expressions.types.processors;

import com.kush.lib.expressions.types.Type;

public abstract class IntReturningTypeProcessor {

    public final int process(Type type) {
        switch (type) {
        case BOOLEAN:
            return handleBoolean();
        case BYTE:
            return handleByte();
        case CHAR:
            return handleChar();
        case INTEGER:
            return handleInt();
        case LONG:
            return handleLong();
        case FLOAT:
            return handleFloat();
        case DOUBLE:
            return handleDouble();
        case STRING:
            return handleString();
        case OBJECT:
            return handleObject();
        default:
            throw new UnsupportedOperationException();
        }
    }

    protected abstract int handleBoolean();

    protected abstract int handleByte();

    protected abstract int handleChar();

    protected abstract int handleInt();

    protected abstract int handleLong();

    protected abstract int handleFloat();

    protected abstract int handleDouble();

    protected abstract int handleString();

    protected abstract int handleObject();
}
