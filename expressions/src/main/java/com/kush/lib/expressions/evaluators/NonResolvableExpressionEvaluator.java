package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;

class NonResolvableExpressionEvaluator<E extends Expression, T> extends BaseExpressionEvaluator<E, T> {

    public NonResolvableExpressionEvaluator(E expression, ExpressionEvaluatorFactory<T> evaluatorFactory) {
        super(expression, evaluatorFactory);
    }
}
