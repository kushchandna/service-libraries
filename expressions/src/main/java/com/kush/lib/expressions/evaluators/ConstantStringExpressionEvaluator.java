package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.Type.STRING;
import static com.kush.lib.expressions.utils.TypedResultFactory.nullResult;
import static com.kush.lib.expressions.utils.TypedResultFactory.stringResult;

import com.kush.lib.expressions.TypedResult;
import com.kush.lib.expressions.types.ConstantStringExpression;

class ConstantStringExpressionEvaluator<T> extends BaseConstantExpressionEvaluator<ConstantStringExpression, T> {

    public ConstantStringExpressionEvaluator(ConstantStringExpression expression) {
        super(expression, STRING);
    }

    @Override
    protected TypedResult evaluateConstantResult(ConstantStringExpression expression) {
        String value = expression.getValue();
        if (value == null) {
            return nullResult(STRING);
        }
        return stringResult(value);
    }
}
