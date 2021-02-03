package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;

abstract class BaseExpressionEvaluator<E extends Expression, T> implements ExpressionEvaluator<T> {

    protected final E expression;
    protected final ExpressionEvaluatorFactory<T> evaluatorFactory;

    public BaseExpressionEvaluator(E expression, ExpressionEvaluatorFactory<T> evaluatorFactory) {
        this.expression = expression;
        this.evaluatorFactory = evaluatorFactory;
    }

    @Override
    public int evaluateInt(T object) throws ExpressionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double evaluateDouble(T object) throws ExpressionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float evaluateFloat(T object) throws ExpressionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long evaluateLong(T object) throws ExpressionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String evaluateString(T object) throws ExpressionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean evaluateBoolean(T object) throws ExpressionException {
        throw new UnsupportedOperationException();
    }
}
