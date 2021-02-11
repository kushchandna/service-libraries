package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.clauses.EqualsExpression;
import com.kush.lib.expressions.types.TypedValue;

class EqualsExpressionEvaluator<T> extends BaseComparisionExpressionEvaluator<EqualsExpression, T> {

    public EqualsExpressionEvaluator(EqualsExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory)
            throws ExpressionException {
        super(expression, evaluatorFactory);
    }

    @Override
    protected boolean evaluateNonNullComparision(TypedValue leftValue, TypedValue rightValue) {
        return leftValue.equals(rightValue);
    }
}
