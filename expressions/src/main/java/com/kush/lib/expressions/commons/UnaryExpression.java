package com.kush.lib.expressions.commons;

import com.kush.lib.expressions.Expression;

public interface UnaryExpression extends Expression {

    Expression getChild();
}
