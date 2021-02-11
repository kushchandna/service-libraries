package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.clauses.GreaterThanEqualsExpression;
import com.kush.lib.expressions.types.TypedValue;

class GreaterThanEqualsExpressionEvaluator<T> extends BaseComparisionExpressionEvaluator<GreaterThanEqualsExpression, T> {

    public GreaterThanEqualsExpressionEvaluator(GreaterThanEqualsExpression expression,
            ExpressionEvaluatorFactory<T> evaluatorFactory) throws ExpressionException {
        super(expression, evaluatorFactory);
    }

    @Override
    protected boolean evaluateNonNullComparision(TypedValue leftValue, TypedValue rightValue) {
        return leftValue.compareTo(rightValue) >= 0;
    }
}
