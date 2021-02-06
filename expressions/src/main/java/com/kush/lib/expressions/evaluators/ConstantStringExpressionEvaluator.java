package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.Type.STRING;
import static com.kush.lib.expressions.utils.TypedResultFactory.nullResult;
import static com.kush.lib.expressions.utils.TypedResultFactory.stringResult;

import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.TypedResult;
import com.kush.lib.expressions.Type;
import com.kush.lib.expressions.types.ConstantStringExpression;

public class ConstantStringExpressionEvaluator<T> extends BaseExpressionEvaluator<ConstantStringExpression, T> {

    private final ConstantStringExpression expression;

    public ConstantStringExpressionEvaluator(ConstantStringExpression expression) {
        this.expression = expression;
    }

    @Override
    public TypedResult evaluate(T object) throws ExpressionException {
        String value = expression.getValue();
        if (value == null) {
            return nullResult(STRING);
        }
        return stringResult(value);
    }

    @Override
    public Type evaluateType() {
        return STRING;
    }
}
