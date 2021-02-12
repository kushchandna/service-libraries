package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.types.Type.BOOLEAN;
import static com.kush.lib.expressions.types.factory.TypedValueFactory.booleanValue;
import static com.kush.lib.expressions.types.factory.TypedValueFactory.nullValue;

import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.clauses.AndExpression;
import com.kush.lib.expressions.types.Type;
import com.kush.lib.expressions.types.TypedValue;

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
        super(expression);
        leftExprEvaluator = evaluatorFactory.create(expression.getLeft());
        validateType(leftExprEvaluator, BOOLEAN, "AND");
        rightExprEvaluator = evaluatorFactory.create(expression.getRight());
        validateType(rightExprEvaluator, BOOLEAN, "AND");
    }

    @Override
    public TypedValue evaluate(T object) throws ExpressionException {
        TypedValue leftValue = leftExprEvaluator.evaluate(object);
        boolean leftResult = leftValue.getBoolean();
        if (!leftValue.isNull() && leftResult == false) {
            // left != null && left == false
            return booleanValue(leftResult);
        }

        // left == null || left == true
        TypedValue rightValue = rightExprEvaluator.evaluate(object);
        boolean rightResult = rightValue.getBoolean();
        if (leftValue.isNull() && rightResult != false) {
            // left == null
            return nullValue(BOOLEAN);
        }

        // left == true
        return rightValue;
    }

    @Override
    public Type evaluateType() {
        return BOOLEAN;
    }
}
