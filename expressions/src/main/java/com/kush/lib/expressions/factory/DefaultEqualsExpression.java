package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.clauses.EqualsExpression;

class DefaultEqualsExpression extends BaseBinomialExpression implements EqualsExpression {

    public DefaultEqualsExpression(Expression left, Expression right) {
        super(left, right);
    }
}
