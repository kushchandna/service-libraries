package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.ExpressionType.BOOLEAN;
import static com.kush.lib.expressions.utils.ExpressionResultFactory.booleanResult;
import static com.kush.lib.expressions.utils.ExpressionResultFactory.nullResult;

import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.ExpressionResult;
import com.kush.lib.expressions.ExpressionType;
import com.kush.lib.expressions.types.OrExpression;

/**
 * true OR true = true
 * true OR false = true
 * true OR null = true
 *
 * false OR true = true
 * false OR false = false
 * false OR null = null
 *
 * null OR true = true
 * null OR false = null
 * null OR null = null
 */
class OrExpressionEvaluator<T> extends BaseExpressionEvaluator<OrExpression, T> {

    private final ExpressionEvaluator<T> leftExprEvaluator;
    private final ExpressionEvaluator<T> rightExprEvaluator;

    public OrExpressionEvaluator(OrExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory)
            throws ExpressionException {
        leftExprEvaluator = evaluatorFactory.create(expression.getLeft());
        validateType(leftExprEvaluator, BOOLEAN, "OR");
        rightExprEvaluator = evaluatorFactory.create(expression.getRight());
        validateType(rightExprEvaluator, BOOLEAN, "OR");
    }

    @Override
    public ExpressionResult evaluate(T object) throws ExpressionException {
        ExpressionResult leftResult = leftExprEvaluator.evaluate(object);
        boolean leftValue = leftResult.getBoolean();
        if (!leftResult.isNull() && leftValue == true) {
            // left != null && left == true
            return booleanResult(true);
        }

        // left == null || left == false
        ExpressionResult rightResult = rightExprEvaluator.evaluate(object);
        boolean rightValue = rightResult.getBoolean();
        if (leftResult.isNull() && rightValue != true) {
            // left == null
            return nullResult(BOOLEAN);
        }

        // left == true
        return rightResult;
    }

    @Override
    public ExpressionType evaluateType() {
        return BOOLEAN;
    }
}