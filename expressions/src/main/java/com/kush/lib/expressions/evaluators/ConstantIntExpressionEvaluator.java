package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.Type.INTEGER;
import static com.kush.lib.expressions.utils.SpecialValues.NULL_INT;
import static com.kush.lib.expressions.utils.TypedValueFactory.intValue;
import static com.kush.lib.expressions.utils.TypedValueFactory.nullValue;

import com.kush.lib.expressions.TypedValue;
import com.kush.lib.expressions.types.ConstantIntExpression;

class ConstantIntExpressionEvaluator<T> extends BaseConstantExpressionEvaluator<ConstantIntExpression, T> {

    public ConstantIntExpressionEvaluator(ConstantIntExpression expression) {
        super(expression, INTEGER);
    }

    @Override
    protected TypedValue evaluateConstantValue(ConstantIntExpression expression) {
        int value = expression.getValue();
        if (value == NULL_INT) {
            return nullValue(INTEGER);
        }
        return intValue(value);
    }
}
