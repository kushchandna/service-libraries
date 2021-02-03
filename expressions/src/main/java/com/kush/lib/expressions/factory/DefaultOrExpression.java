package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.types.OrExpression;

class DefaultOrExpression extends BaseBinomialExpression implements OrExpression {

    public DefaultOrExpression(Expression left, Expression right) {
        super(left, right);
    }
}
