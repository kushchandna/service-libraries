package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.clauses.GreaterThanExpression;

class DefaultGreaterThanExpression extends BaseBinomialExpression implements GreaterThanExpression {

    public DefaultGreaterThanExpression(Expression left, Expression right) {
        super(left, right);
    }
}
