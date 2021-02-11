package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.clauses.GreaterThanEqualsExpression;

class DefaultGreaterThanEqualsExpression extends BaseBinomialExpression implements GreaterThanEqualsExpression {

    public DefaultGreaterThanEqualsExpression(Expression left, Expression right) {
        super(left, right);
    }
}
