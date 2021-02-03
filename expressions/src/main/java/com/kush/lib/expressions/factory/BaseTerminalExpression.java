package com.kush.lib.expressions.factory;

import static java.util.Collections.emptyList;

import java.util.Collection;

import com.kush.lib.expressions.Expression;

abstract class BaseTerminalExpression implements Expression {

    @Override
    public Collection<Expression> getChildren() {
        return emptyList();
    }
}
