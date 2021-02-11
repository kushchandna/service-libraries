package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.clauses.NotExpression;

class DefaultNotExpression extends BaseUnaryExpression implements NotExpression {

    public DefaultNotExpression(Expression child) {
        super(child);
    }
}
