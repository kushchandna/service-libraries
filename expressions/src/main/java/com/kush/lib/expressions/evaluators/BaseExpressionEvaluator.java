package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.ExpressionException.exceptionWithMessage;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionType;

abstract class BaseExpressionEvaluator<E extends Expression, T> implements ExpressionEvaluator<T> {

    // utility methods

    protected final void validateSameTypeOnBothSides(ExpressionEvaluator<T> leftExprEvaluator,
            ExpressionEvaluator<T> rightExprEvaluator, String operation) {
        ExpressionType leftType = leftExprEvaluator.evaluateType();
        ExpressionType rightType = rightExprEvaluator.evaluateType();
        if (leftType != rightType) {
            exceptionWithMessage("Both sides of an %s expression should be same, but got %s and %s", operation, leftType,
                    rightType);
        }
    }

    protected final void validateType(ExpressionEvaluator<T> expressionEvaluator, ExpressionType expressionType,
            String operation) {
        ExpressionType type = expressionEvaluator.evaluateType();
        if (type != expressionType) {
            exceptionWithMessage("%s operation can only accept type %s, but got %s", operation, expressionType, type);
        }
    }
}
