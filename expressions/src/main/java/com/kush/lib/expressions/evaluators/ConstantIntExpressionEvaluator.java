package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.types.Type.INTEGER;
import static com.kush.lib.expressions.types.TypedValueFactory.intValue;
import static com.kush.lib.expressions.types.TypedValueFactory.nullValue;
import static com.kush.lib.expressions.utils.SpecialValues.NULL_INT;

import com.kush.lib.expressions.clauses.ConstantIntExpression;
import com.kush.lib.expressions.types.TypedValue;

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
