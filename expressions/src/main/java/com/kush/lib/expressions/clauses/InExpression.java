package com.kush.lib.expressions.clauses;

import java.util.Collection;

import com.kush.lib.expressions.Expression;

public interface InExpression extends Expression {

    Expression getTarget();

    Collection<Expression> getInExpressions();
}
