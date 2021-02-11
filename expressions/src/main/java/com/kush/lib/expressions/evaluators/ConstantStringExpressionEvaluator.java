package com.kush.lib.expressions.evaluators;

import static com.kush.lib.expressions.Type.STRING;
import static com.kush.lib.expressions.utils.TypedValueFactory.nullValue;
import static com.kush.lib.expressions.utils.TypedValueFactory.stringValue;

import com.kush.lib.expressions.TypedValue;
import com.kush.lib.expressions.types.ConstantStringExpression;

class ConstantStringExpressionEvaluator<T> extends BaseConstantExpressionEvaluator<ConstantStringExpression, T> {

    public ConstantStringExpressionEvaluator(ConstantStringExpression expression) {
        super(expression, STRING);
    }

    @Override
    protected TypedValue evaluateConstantValue(ConstantStringExpression expression) {
        String value = expression.getValue();
        if (value == null) {
            return nullValue(STRING);
        }
        return stringValue(value);
    }
}
