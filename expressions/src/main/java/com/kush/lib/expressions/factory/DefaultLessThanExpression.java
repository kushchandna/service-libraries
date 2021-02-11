package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.clauses.LessThanExpression;

class DefaultLessThanExpression extends BaseBinomialExpression implements LessThanExpression {

    public DefaultLessThanExpression(Expression left, Expression right) {
        super(left, right);
    }
}
