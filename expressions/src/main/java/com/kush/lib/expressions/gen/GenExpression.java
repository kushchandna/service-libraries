package com.kush.lib.expressions.gen;

public class GenExpression {

    private final String name;
    private final GenExpressionFields expressionFields;

    public GenExpression(String name, GenExpressionFields expressionFields) {
        this.name = name;
        this.expressionFields = expressionFields;
    }

    public String getName() {
        return name;
    }

    public GenExpressionFields getExpressionFields() {
        return expressionFields;
    }
}
