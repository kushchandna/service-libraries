package com.kush.lib.expressions.commons;

import com.kush.lib.expressions.Expression;

public interface RangeExpression extends Expression {

    Expression getStart();

    boolean isStartInclusive();

    Expression getEnd();

    boolean isEndInclusive();
}
