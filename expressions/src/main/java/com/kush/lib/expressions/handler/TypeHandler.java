package com.kush.lib.expressions.handler;

import com.kush.lib.expressions.Type;

public abstract class TypeHandler<T> {

    public final T handle(Type type) {
        switch (type) {
        case BOOLEAN:
            return handleBoolean();
        case DOUBLE:
            return handleDouble();
        case FLOAT:
            return handleFloat();
        case INTEGER:
            return handleInteger();
        case LONG:
            return handleLong();
        case STRING:
            return handleString();
        default:
            throw new IllegalStateException();
        }
    }

    protected abstract T handleString();

    protected abstract T handleLong();

    protected abstract T handleInteger();

    protected abstract T handleFloat();

    protected abstract T handleDouble();

    protected abstract T handleBoolean();
}
