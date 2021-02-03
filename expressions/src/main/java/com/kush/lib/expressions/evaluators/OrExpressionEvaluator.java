package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.types.OrExpression;

class OrExpressionEvaluator<T> extends BaseExpressionEvaluator<OrExpression, T> {

    public OrExpressionEvaluator(OrExpression expression, ExpressionEvaluatorFactory<T> evaluatorFactory) {
        super(expression, evaluatorFactory);
    }

    @Override
    public boolean evaluateBoolean(T object) throws ExpressionException {
        ExpressionEvaluator<T> leftExprEvaluator = evaluatorFactory.create(expression.getLeft());
        boolean leftResult = leftExprEvaluator.evaluateBoolean(object);

        ExpressionEvaluator<T> rightExprEvaluator = evaluatorFactory.create(expression.getRight());
        boolean rightResult = rightExprEvaluator.evaluateBoolean(object);

        return leftResult || rightResult;
    }
}
