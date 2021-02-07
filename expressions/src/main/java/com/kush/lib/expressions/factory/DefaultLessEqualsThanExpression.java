package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.types.LessThanEqualsExpression;

class DefaultLessEqualsThanExpression extends BaseBinomialExpression implements LessThanEqualsExpression {

    public DefaultLessEqualsThanExpression(Expression left, Expression right) {
        super(left, right);
    }
}
