package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.clauses.LessThanEqualsExpression;

class DefaultLessThanEqualsExpression extends BaseBinomialExpression implements LessThanEqualsExpression {

    public DefaultLessThanEqualsExpression(Expression left, Expression right) {
        super(left, right);
    }
}
