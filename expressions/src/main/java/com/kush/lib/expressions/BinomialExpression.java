package com.kush.lib.indexing.expressions;

public interface BinomialExpression extends Expression {

    Expression getLeft();

    Expression getRight();
}
