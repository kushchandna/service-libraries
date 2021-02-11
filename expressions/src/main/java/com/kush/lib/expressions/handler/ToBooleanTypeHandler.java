package com.kush.lib.expressions.handler;

import com.kush.lib.expressions.types.Type;

public abstract class ToBooleanTypeHandler {

    public final boolean handle(Type type) {
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

    protected abstract boolean handleString();

    protected abstract boolean handleLong();

    protected abstract boolean handleInteger();

    protected abstract boolean handleFloat();

    protected abstract boolean handleDouble();

    protected abstract boolean handleBoolean();
}
