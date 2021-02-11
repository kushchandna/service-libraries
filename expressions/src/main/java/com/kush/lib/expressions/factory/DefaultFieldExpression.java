package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.clauses.FieldExpression;

class DefaultFieldExpression extends BaseTerminalExpression implements FieldExpression {

    private final String fieldName;

    public DefaultFieldExpression(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
