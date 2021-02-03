package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.types.ConstantIntExpression;

class DefaultConstantIntExpression extends BaseTerminalExpression implements ConstantIntExpression {

    private final int value;

    public DefaultConstantIntExpression(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
