package com.kush.lib.expressions.commons;

import com.kush.lib.expressions.Expression;

public interface BinomialExpression extends Expression {

    Expression getLeft();

    Expression getRight();
}
