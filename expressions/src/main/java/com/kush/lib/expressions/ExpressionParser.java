package com.kush.lib.expressions;

public interface ExpressionParser<T> {

    Expression parse(T input) throws ExpressionParsingFailedException;
}
