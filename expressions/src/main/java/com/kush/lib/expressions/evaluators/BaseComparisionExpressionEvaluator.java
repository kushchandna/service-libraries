package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.Type.BOOLEAN;
import static com.kush.lib.expressions.utils.TypedValueFactory.booleanValue;
import static com.kush.lib.expressions.utils.TypedValueFactory.nullValue;

import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.Type;
import com.kush.lib.expressions.TypedValue;
import com.kush.lib.expressions.commons.ComparisionExpression;

/**
 * null compared with null is null
 * null compared with non-null is also null
 */
abstract class BaseComparisionExpressionEvaluator<E extends ComparisionExpression, T> extends BaseExpressionEvaluator<E, T> {

    private final ExpressionEvaluator<T> leftExprEvaluator;
    private final ExpressionEvaluator<T> rightExprEvaluator;

    public BaseComparisionExpressionEvaluator(E expression, ExpressionEvaluatorFactory<T> evaluatorFactory)
            throws ExpressionException {
        super(expression);
        leftExprEvaluator = evaluatorFactory.create(expression.getLeft());
        rightExprEvaluator = evaluatorFactory.create(expression.getRight());
        validateSameTypeOnBothSides(leftExprEvaluator, rightExprEvaluator, "EQUALS");
    }

    @Override
    public TypedValue evaluate(T object) throws ExpressionException {
        TypedValue leftValue = leftExprEvaluator.evaluate(object);
        TypedValue rightValue = rightExprEvaluator.evaluate(object);
        if (leftValue.isNull() || rightValue.isNull()) {
            return nullValue(BOOLEAN);
        }
        return booleanValue(evaluateNonNullComparision(leftValue, rightValue));
    }

    @Override
    public Type evaluateType() {
        return BOOLEAN;
    }

    protected abstract boolean evaluateNonNullComparision(TypedValue leftValue, TypedValue rightValue);
}
