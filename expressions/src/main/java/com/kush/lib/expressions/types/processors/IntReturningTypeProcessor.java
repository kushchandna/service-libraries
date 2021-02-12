package com.kush.lib.expressions.types.processors;

import com.kush.lib.expressions.types.Type;

public abstract class IntReturningTypeProcessor {

    public final int process(Type type) {
        switch (type) {
        case BOOLEAN:
            return handleBoolean();
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
        default:
            throw new UnsupportedOperationException();
        }
    }

    protected abstract int handleBoolean();

    protected abstract int handleInt();

    protected abstract int handleLong();

    protected abstract int handleFloat();

    protected abstract int handleDouble();

    protected abstract int handleString();
}
