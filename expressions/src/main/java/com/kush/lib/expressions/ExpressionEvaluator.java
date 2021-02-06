package com.kush.lib.expressions;

public interface ExpressionEvaluator<T> {

    TypedResult evaluate(T object) throws ExpressionException;

    Type evaluateType();
}
