package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.Type;
import com.kush.lib.expressions.TypedResult;
import com.kush.lib.expressions.commons.ConstantExpression;

abstract class BaseConstantExpressionEvaluator<E extends ConstantExpression, T> extends BaseExpressionEvaluator<E, T> {

    private final TypedResult result;
    private final Type type;

    public BaseConstantExpressionEvaluator(E expression, Type type) {
        super(expression);
        this.type = type;
        result = evaluateConstantResult(expression);
    }

    @Override
    public TypedResult evaluate(T object) throws ExpressionException {
        return result;
    }

    @Override
    public Type evaluateType() {
        return type;
    }

    protected abstract TypedResult evaluateConstantResult(E expression);
}
