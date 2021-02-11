package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.clauses.LessThanExpression;
import com.kush.lib.expressions.types.TypedValue;

class LessThanExpressionEvaluator<T> extends BaseComparisionExpressionEvaluator<LessThanExpression, T> {

    public LessThanExpressionEvaluator(LessThanExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory)
            throws ExpressionException {
        super(expression, evaluatorFactory);
    }

    @Override
    protected boolean evaluateNonNullComparision(TypedValue leftValue, TypedValue rightValue) {
        return leftValue.compareTo(rightValue) < 0;
    }
}
