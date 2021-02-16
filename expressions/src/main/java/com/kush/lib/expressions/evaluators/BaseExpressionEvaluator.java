package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.ExpressionException.exceptionWithMessage;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.types.Type;

abstract class BaseExpressionEvaluator<E extends Expression, T> implements ExpressionEvaluator<T> {

    protected final E expression;

    public BaseExpressionEvaluator(E expression) {
        this.expression = expression;
    }

    // utility methods

    protected final void validateSameTypeOnBothSides(ExpressionEvaluator<T> leftExprEvaluator,
            ExpressionEvaluator<T> rightExprEvaluator, String operation) throws ExpressionException {
        Type leftType = leftExprEvaluator.evaluateType();
        Type rightType = rightExprEvaluator.evaluateType();
        if (leftType != rightType) {
            throw exceptionWithMessage("Both sides of an %s expression should be same, but got %s and %s",
                    operation, leftType, rightType);
        }
    }

    protected final void validateType(ExpressionEvaluator<T> expressionEvaluator, Type expressionType,
            String operation) throws ExpressionException {
        Type type = expressionEvaluator.evaluateType();
        if (type != expressionType) {
            throw exceptionWithMessage("%s operation can only accept type %s, but got %s",
                    operation, expressionType, type);
        }
    }
}
