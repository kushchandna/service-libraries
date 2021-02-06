package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.Type.INTEGER;
import static com.kush.lib.expressions.utils.SpecialValues.NULL_INT;
import static com.kush.lib.expressions.utils.TypedResultFactory.intResult;
import static com.kush.lib.expressions.utils.TypedResultFactory.nullResult;

import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.Type;
import com.kush.lib.expressions.TypedResult;
import com.kush.lib.expressions.types.ConstantIntExpression;

class ConstantIntExpressionEvaluator<T> extends BaseExpressionEvaluator<ConstantIntExpression, T> {

    private final ConstantIntExpression expression;

    public ConstantIntExpressionEvaluator(ConstantIntExpression expression) {
        this.expression = expression;
    }

    @Override
    public TypedResult evaluate(T object) throws ExpressionException {
        int value = expression.getValue();
        if (value == NULL_INT) {
            return nullResult(INTEGER);
        }
        return intResult(value);
    }

    @Override
    public Type evaluateType() {
        return INTEGER;
    }
}
