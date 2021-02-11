package com.kush.lib.expressions.handler;

import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.types.Type;

public abstract class TypeHandler<T> {

    public final T handle(Type type) throws ExpressionException {
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

    protected abstract T handleString() throws ExpressionException;

    protected abstract T handleLong() throws ExpressionException;

    protected abstract T handleInteger() throws ExpressionException;

    protected abstract T handleFloat() throws ExpressionException;

    protected abstract T handleDouble() throws ExpressionException;

    protected abstract T handleBoolean() throws ExpressionException;
}
