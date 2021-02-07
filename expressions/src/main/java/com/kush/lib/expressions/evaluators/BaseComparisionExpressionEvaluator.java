package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.Type.BOOLEAN;
import static com.kush.lib.expressions.utils.TypedResultFactory.booleanResult;
import static com.kush.lib.expressions.utils.TypedResultFactory.nullResult;

import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.Type;
import com.kush.lib.expressions.TypedResult;
import com.kush.lib.expressions.commons.ComparisionExpression;

/**
 * null compared with null is null
 */
abstract class BaseComparisionExpressionEvaluator<E extends ComparisionExpression, T> extends BaseExpressionEvaluator<E, T> {

    private final ExpressionEvaluator<T> leftExprEvaluator;
    private final ExpressionEvaluator<T> rightExprEvaluator;

    public BaseComparisionExpressionEvaluator(ComparisionExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory)
            throws ExpressionException {
        leftExprEvaluator = evaluatorFactory.create(expression.getLeft());
        rightExprEvaluator = evaluatorFactory.create(expression.getRight());
        validateSameTypeOnBothSides(leftExprEvaluator, rightExprEvaluator, "EQUALS");
    }

    @Override
    public TypedResult evaluate(T object) throws ExpressionException {
        TypedResult leftResult = leftExprEvaluator.evaluate(object);
        TypedResult rightResult = rightExprEvaluator.evaluate(object);
        if (leftResult.isNull()) {
            if (rightResult.isNull()) {
                return nullResult(BOOLEAN);
            } else {
                return booleanResult(evaluateWithLeftNullRightNonNull(rightResult));
            }
        }

        // left != null
        if (rightResult.isNull()) {
            return booleanResult(evaluateWithRightNullLeftNonNull(leftResult));
        }

        return booleanResult(evaluateLeftRightNonNull(leftResult, rightResult));
    }

    @Override
    public Type evaluateType() {
        return BOOLEAN;
    }

    protected abstract boolean evaluateWithLeftNullRightNonNull(TypedResult rightResult);

    protected abstract boolean evaluateWithRightNullLeftNonNull(TypedResult leftResult);

    protected abstract boolean evaluateLeftRightNonNull(TypedResult leftResult, TypedResult rightResult);
}
