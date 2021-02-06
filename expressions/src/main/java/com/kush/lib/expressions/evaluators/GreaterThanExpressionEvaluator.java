package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.Type.BOOLEAN;
import static com.kush.lib.expressions.utils.TypedResultFactory.booleanResult;
import static com.kush.lib.expressions.utils.TypedResultFactory.nullResult;

import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.Type;
import com.kush.lib.expressions.TypedResult;
import com.kush.lib.expressions.types.GreaterThanExpression;


/**
 * null > null = null
 */
class GreaterThanExpressionEvaluator<T> extends BaseExpressionEvaluator<GreaterThanExpression, T> {

    private final ExpressionEvaluator<T> leftExprEvaluator;
    private final ExpressionEvaluator<T> rightExprEvaluator;
    private final boolean isNullHigh;

    public GreaterThanExpressionEvaluator(GreaterThanExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory,
            boolean isNullHigh) throws ExpressionException {
        leftExprEvaluator = evaluatorFactory.create(expression.getLeft());
        rightExprEvaluator = evaluatorFactory.create(expression.getRight());
        validateSameTypeOnBothSides(leftExprEvaluator, rightExprEvaluator, "EQUALS");
        this.isNullHigh = isNullHigh;
    }

    @Override
    public TypedResult evaluate(T object) throws ExpressionException {
        TypedResult leftResult = leftExprEvaluator.evaluate(object);
        TypedResult rightResult = rightExprEvaluator.evaluate(object);
        if (leftResult.isNull()) {
            if (rightResult.isNull()) {
                return nullResult(BOOLEAN);
            } else {
                return booleanResult(isNullHigh);
            }
        }

        // left != null
        if (rightResult.isNull()) {
            return booleanResult(!isNullHigh);
        }

        return booleanResult(leftResult.compareTo(rightResult) > 0);
    }

    @Override
    public Type evaluateType() {
        return BOOLEAN;
    }
}
