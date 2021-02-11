package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.clauses.ConstantStringExpression;

class DefaultConstantStringExpression extends BaseTerminalExpression implements ConstantStringExpression {

    private final String value;

    public DefaultConstantStringExpression(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
