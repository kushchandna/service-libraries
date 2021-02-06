package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.ExpressionType.STRING;
import static com.kush.lib.expressions.utils.ExpressionResultFactory.nullResult;
import static com.kush.lib.expressions.utils.ExpressionResultFactory.stringResult;

import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.ExpressionResult;
import com.kush.lib.expressions.ExpressionType;
import com.kush.lib.expressions.types.ConstantStringExpression;

public class ConstantStringExpressionEvaluator<T> extends BaseExpressionEvaluator<ConstantStringExpression, T> {

    private final ConstantStringExpression expression;

    public ConstantStringExpressionEvaluator(ConstantStringExpression expression) {
        this.expression = expression;
    }

    @Override
    public ExpressionResult evaluate(T object) throws ExpressionException {
        String value = expression.getValue();
        if (value == null) {
            return nullResult(STRING);
        }
        return stringResult(value);
    }

    @Override
    public ExpressionType evaluateType() {
        return STRING;
    }
}
