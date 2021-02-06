package com.kush.lib.expressions.evaluators;

import java.util.function.Function;

import com.kush.lib.expressions.Accessor;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.ExpressionResult;
import com.kush.lib.expressions.ExpressionType;
import com.kush.lib.expressions.types.FieldExpression;

public class FieldExpressionEvaluator<T> extends BaseExpressionEvaluator<FieldExpression, T> {

    private final Accessor<T> accessor;
    private final ExpressionType expressionType;

    public FieldExpressionEvaluator(FieldExpression expression, Function<String, Accessor<T>> accessorGetter,
            Function<String, ExpressionType> typeGetter) {
        String fieldName = expression.getFieldName();
        accessor = accessorGetter.apply(fieldName);
        expressionType = typeGetter.apply(fieldName);
    }

    @Override
    public ExpressionResult evaluate(T object) throws ExpressionException {
        return accessor.access(object);
    }

    @Override
    public ExpressionType evaluateType() {
        return expressionType;
    }
}
