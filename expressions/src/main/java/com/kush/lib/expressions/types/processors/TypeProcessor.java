package com.kush.lib.expressions.types.processors;

import com.kush.lib.expressions.types.Type;

public abstract class TypeProcessor<T> {

    public final T process(Type type) {
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

    protected abstract T handleBoolean();

    protected abstract T handleInt();

    protected abstract T handleLong();

    protected abstract T handleFloat();

    protected abstract T handleDouble();

    protected abstract T handleString();
}
