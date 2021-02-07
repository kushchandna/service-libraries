package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.Type.INTEGER;
import static com.kush.lib.expressions.utils.SpecialValues.NULL_INT;
import static com.kush.lib.expressions.utils.TypedResultFactory.intResult;
import static com.kush.lib.expressions.utils.TypedResultFactory.nullResult;

import com.kush.lib.expressions.TypedResult;
import com.kush.lib.expressions.types.ConstantIntExpression;

class ConstantIntExpressionEvaluator<T> extends BaseConstantExpressionEvaluator<ConstantIntExpression, T> {

    public ConstantIntExpressionEvaluator(ConstantIntExpression expression) {
        super(expression, INTEGER);
    }

    @Override
    protected TypedResult evaluateConstantResult(ConstantIntExpression expression) {
        int value = expression.getValue();
        if (value == NULL_INT) {
            return nullResult(INTEGER);
        }
        return intResult(value);
    }
}
