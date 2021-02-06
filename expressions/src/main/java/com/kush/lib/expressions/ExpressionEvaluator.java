package com.kush.lib.expressions;

public interface ExpressionEvaluator<T> {

    ExpressionResult evaluate(T object) throws ExpressionException;

    ExpressionType evaluateType();
}
