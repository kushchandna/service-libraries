package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.Type.BOOLEAN;
import static com.kush.lib.expressions.utils.TypedResultFactory.booleanResult;

import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.TypedResult;
import com.kush.lib.expressions.Type;
import com.kush.lib.expressions.types.EqualsExpression;


/**
 * null EQUALS null = true
 * null EQUALS non-null = false
 * non-null EQUALS null = false
 */
public class EqualsExpressionEvaluator<T> extends BaseExpressionEvaluator<EqualsExpression, T> {

    private final ExpressionEvaluator<T> leftExprEvaluator;
    private final ExpressionEvaluator<T> rightExprEvaluator;

    public EqualsExpressionEvaluator(EqualsExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory)
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
                return booleanResult(true);
            } else {
                return booleanResult(false);
            }
        }

        // left != null
        if (rightResult.isNull()) {
            return booleanResult(false);
        }

        return booleanResult(leftResult.equals(rightResult));
    }

    @Override
    public Type evaluateType() {
        return BOOLEAN;
    }
}
