package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.types.Type.BOOLEAN;
import static com.kush.lib.expressions.types.factory.TypedValueFactory.booleanValue;
import static com.kush.lib.expressions.types.factory.TypedValueFactory.nullValue;

import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.clauses.NotExpression;
import com.kush.lib.expressions.types.Type;
import com.kush.lib.expressions.types.TypedValue;

/**
 * !null = null
 * !true = false
 * !false = true
 */
class NotExpressionEvaluator<T> extends BaseExpressionEvaluator<NotExpression, T> {

    private final ExpressionEvaluator<T> evaluator;

    public NotExpressionEvaluator(NotExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory)
            throws ExpressionException {
        super(expression);
        evaluator = evaluatorFactory.create(expression.getChild());
        validateType(evaluator, BOOLEAN, "NOT");
    }

    @Override
    public TypedValue evaluate(T object) throws ExpressionException {
        TypedValue value = evaluator.evaluate(object);
        if (value.isNull()) {
            return nullValue(BOOLEAN);
        }
        return booleanValue(!value.getBoolean());
    }

    @Override
    public Type evaluateType() {
        return BOOLEAN;
    }
}
