package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.types.GreaterThanExpression;

class DefaultGreaterEqualsThanExpression extends BaseBinomialExpression implements GreaterThanExpression {

    public DefaultGreaterEqualsThanExpression(Expression left, Expression right) {
        super(left, right);
    }
}
