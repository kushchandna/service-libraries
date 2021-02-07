package com.kush.lib.expressions.factory;

import static java.util.Arrays.asList;

import java.util.Collection;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.commons.BinomialExpression;

abstract class BaseBinomialExpression implements BinomialExpression {

    private final Expression left;
    private final Expression right;

    public BaseBinomialExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Expression getLeft() {
        return left;
    }

    @Override
    public Expression getRight() {
        return right;
    }

    @Override
    public Collection<Expression> getChildren() {
        return asList(left, right);
    }
}