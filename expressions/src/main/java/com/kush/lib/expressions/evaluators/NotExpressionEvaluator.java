package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.Type.BOOLEAN;
import static com.kush.lib.expressions.utils.TypedResultFactory.booleanResult;
import static com.kush.lib.expressions.utils.TypedResultFactory.nullResult;

import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.TypedResult;
import com.kush.lib.expressions.Type;
import com.kush.lib.expressions.types.NotExpression;

/**
 * !null = null
 * !true = false
 * !false = true
 */
public class NotExpressionEvaluator<T> extends BaseExpressionEvaluator<NotExpression, T> {

    private final ExpressionEvaluator<T> evaluator;

    public NotExpressionEvaluator(NotExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory)
            throws ExpressionException {
        evaluator = evaluatorFactory.create(expression.getChild());
        validateType(evaluator, BOOLEAN, "NOT");
    }

    @Override
    public TypedResult evaluate(T object) throws ExpressionException {
        TypedResult result = evaluator.evaluate(object);
        if (result.isNull()) {
            return nullResult(BOOLEAN);
        }
        return booleanResult(!result.getBoolean());
    }

    @Override
    public Type evaluateType() {
        return BOOLEAN;
    }
}
