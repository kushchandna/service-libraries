package com.kush.lib.expressions;

public interface ExpressionEvaluatorFactory<T> {

    ExpressionEvaluator<T> create(Expression expression) throws ExpressionException;
}
