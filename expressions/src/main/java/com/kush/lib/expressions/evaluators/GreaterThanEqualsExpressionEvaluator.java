package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.TypedResult;
import com.kush.lib.expressions.types.GreaterThanEqualsExpression;

class GreaterThanEqualsExpressionEvaluator<T> extends BaseComparisionExpressionEvaluator<GreaterThanEqualsExpression, T> {

    private final boolean isNullHigh;

    public GreaterThanEqualsExpressionEvaluator(GreaterThanEqualsExpression expression,
            ExpressionEvaluatorFactory<T> evaluatorFactory, boolean isNullHigh) throws ExpressionException {
        super(expression, evaluatorFactory);
        this.isNullHigh = isNullHigh;
    }

    @Override
    protected boolean evaluateWithLeftNullRightNonNull(TypedResult rightResult) {
        return isNullHigh;
    }

    @Override
    protected boolean evaluateWithRightNullLeftNonNull(TypedResult leftResult) {
        return !isNullHigh;
    }

    @Override
    protected boolean evaluateLeftRightNonNull(TypedResult leftResult, TypedResult rightResult) {
        return leftResult.compareTo(rightResult) >= 0;
    }
}
