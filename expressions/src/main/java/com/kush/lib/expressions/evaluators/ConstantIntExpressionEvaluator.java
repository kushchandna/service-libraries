package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.ExpressionType.INTEGER;
import static com.kush.lib.expressions.utils.ExpressionResultFactory.intResult;
import static com.kush.lib.expressions.utils.ExpressionResultFactory.nullResult;
import static com.kush.lib.expressions.utils.SpecialValues.NULL_INT;

import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.ExpressionResult;
import com.kush.lib.expressions.ExpressionType;
import com.kush.lib.expressions.types.ConstantIntExpression;

public class ConstantIntExpressionEvaluator<T> extends BaseExpressionEvaluator<ConstantIntExpression, T> {

    private final ConstantIntExpression expression;

    public ConstantIntExpressionEvaluator(ConstantIntExpression expression) {
        this.expression = expression;
    }

    @Override
    public ExpressionResult evaluate(T object) throws ExpressionException {
        int value = expression.getValue();
        if (value == NULL_INT) {
            return nullResult(INTEGER);
        }
        return intResult(value);
    }

    @Override
    public ExpressionType evaluateType() {
        return INTEGER;
    }
}
