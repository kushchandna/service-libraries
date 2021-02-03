package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.types.NotExpression;

public class NotExpressionEvaluator<T> extends BaseExpressionEvaluator<NotExpression, T> {

    public NotExpressionEvaluator(NotExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory) {
        super(expression, evaluatorFactory);
    }

    @Override
    public boolean evaluateBoolean(T object) throws ExpressionException {
        ExpressionEvaluator<T> evaluator = evaluatorFactory.create(expression.getChild());
        boolean value = evaluator.evaluateBoolean(object);
        return !value;
    }
}
