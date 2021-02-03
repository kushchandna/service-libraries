package com.kush.lib;

import com.kush.lib.expressions.Expression;

public interface ExpressionParser<T> {

    Expression parse(T input) throws ExpressionParsingFailedException;
}
