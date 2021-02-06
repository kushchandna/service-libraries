package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.Type.BOOLEAN;
import static com.kush.lib.expressions.utils.TypedResultFactory.booleanResult;
import static com.kush.lib.expressions.utils.TypedResultFactory.nullResult;

import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.TypedResult;
import com.kush.lib.expressions.Type;
import com.kush.lib.expressions.types.AndExpression;

/**
 * true AND true = true
 * true AND false = false
 * true AND null = null
 *
 * false AND true = false
 * false AND false = false
 * false AND null = false
 *
 * null AND true = null
 * null AND false = false
 * null AND null = null
 */
class AndExpressionEvaluator<T> extends BaseExpressionEvaluator<AndExpression, T> {

    private final ExpressionEvaluator<T> leftExprEvaluator;
    private final ExpressionEvaluator<T> rightExprEvaluator;

    public AndExpressionEvaluator(AndExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory)
            throws ExpressionException {
        leftExprEvaluator = evaluatorFactory.create(expression.getLeft());
        validateType(leftExprEvaluator, BOOLEAN, "AND");
        rightExprEvaluator = evaluatorFactory.create(expression.getRight());
        validateType(rightExprEvaluator, BOOLEAN, "AND");
    }

    @Override
    public TypedResult evaluate(T object) throws ExpressionException {
        TypedResult leftResult = leftExprEvaluator.evaluate(object);
        boolean leftValue = leftResult.getBoolean();
        if (!leftResult.isNull() && leftValue == false) {
            // left != null && left == false
            return booleanResult(leftValue);
        }

        // left == null || left == true
        TypedResult rightResult = rightExprEvaluator.evaluate(object);
        boolean rightValue = rightResult.getBoolean();
        if (leftResult.isNull() && rightValue != false) {
            // left == null
            return nullResult(BOOLEAN);
        }

        // left == true
        return rightResult;
    }

    @Override
    public Type evaluateType() {
        return BOOLEAN;
    }
}
