package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.clauses.FieldExpression;

public interface FieldExpressionEvaluatorFactory<T> {

    FieldExpressionEvaluator<T> create(FieldExpression expression) throws ExpressionException;
}
