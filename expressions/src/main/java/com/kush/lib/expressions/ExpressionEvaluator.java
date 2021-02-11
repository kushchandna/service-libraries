package com.kush.lib.expressions;

public interface ExpressionEvaluator<T> {

    TypedValue evaluate(T object) throws ExpressionException;

    Type evaluateType();
}
