package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.types.EqualsExpression;

public class EqualsExpressionEvaluator<T> extends BaseExpressionEvaluator<EqualsExpression, T> {

    public EqualsExpressionEvaluator(EqualsExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory) {
        super(expression, evaluatorFactory);
    }

    @Override
    public boolean evaluateBoolean(T object) throws ExpressionException {


        return super.evaluateBoolean(object);
    }
}
