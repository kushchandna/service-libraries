package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.TypedResult;
import com.kush.lib.expressions.types.EqualsExpression;


/**
 * null EQUALS null = null
 * null EQUALS non-null = false
 * non-null EQUALS null = false
 */
class EqualsExpressionEvaluator<T> extends BaseComparisionExpressionEvaluator<EqualsExpression, T> {

    public EqualsExpressionEvaluator(EqualsExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory)
            throws ExpressionException {
        super(expression, evaluatorFactory);
    }

    @Override
    protected boolean evaluateWithLeftNullRightNonNull(TypedResult rightResult) {
        return false;
    }

    @Override
    protected boolean evaluateWithRightNullLeftNonNull(TypedResult leftResult) {
        return false;
    }

    @Override
    protected boolean evaluateLeftRightNonNull(TypedResult leftResult, TypedResult rightResult) {
        return leftResult.equals(rightResult);
    }
}
