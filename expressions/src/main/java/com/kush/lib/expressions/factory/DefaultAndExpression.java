package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.clauses.AndExpression;

class DefaultAndExpression extends BaseBinomialExpression implements AndExpression {

    public DefaultAndExpression(Expression left, Expression right) {
        super(left, right);
    }
}
