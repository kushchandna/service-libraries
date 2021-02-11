package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.clauses.GreaterThanExpression;
import com.kush.lib.expressions.types.TypedValue;

class GreaterThanExpressionEvaluator<T> extends BaseComparisionExpressionEvaluator<GreaterThanExpression, T> {

    public GreaterThanExpressionEvaluator(GreaterThanExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory)
            throws ExpressionException {
        super(expression, evaluatorFactory);
    }

    @Override
    protected boolean evaluateNonNullComparision(TypedValue leftValue, TypedValue rightValue) {
        return leftValue.compareTo(rightValue) > 0;
    }
}
