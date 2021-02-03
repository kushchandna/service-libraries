package com.kush.lib;

import com.kush.lib.expressions.AndExpression;
import com.kush.lib.expressions.FieldExpression;
import com.kush.lib.expressions.NotExpression;
import com.kush.lib.expressions.OrExpression;

public class ExpressionProcessor {

    private final ExpressionFactory expressionFactory;

    public ExpressionProcessor(ExpressionFactory expressionFactory) {
        this.expressionFactory = expressionFactory;
    }

    public FieldExpression process(FieldExpression expression) {
        return expressionFactory.createFieldExpression(expression.getFieldName());
    }

    public AndExpression process(AndExpression expression) {
        return expressionFactory.createAndExpression(expression.getLeft(), expression.getRight());
    }

    public OrExpression process(OrExpression expression) {
        return expressionFactory.createOrExpression(expression.getLeft(), expression.getRight());
    }

    public NotExpression process(NotExpression expression) {
        return expressionFactory.createNotExpression(expression.getChild());
    }
}
