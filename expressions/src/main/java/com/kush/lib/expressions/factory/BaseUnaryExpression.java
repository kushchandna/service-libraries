package com.kush.lib.expressions.factory;

import static java.util.Collections.singletonList;

import java.util.Collection;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.commons.UnaryExpression;

abstract class BaseUnaryExpression implements UnaryExpression {

    private final Expression child;

    public BaseUnaryExpression(Expression child) {
        this.child = child;
    }

    @Override
    public Expression getChild() {
        return child;
    }

    @Override
    public Collection<Expression> getChildren() {
        return singletonList(child);
    }
}
