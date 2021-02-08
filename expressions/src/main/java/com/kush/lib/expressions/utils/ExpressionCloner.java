package com.kush.lib.expressions.utils;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.ExpressionFactory;
import com.kush.lib.expressions.ExpressionProcessor;
import com.kush.lib.expressions.types.AndExpression;
import com.kush.lib.expressions.types.ConstantIntExpression;
import com.kush.lib.expressions.types.ConstantStringExpression;
import com.kush.lib.expressions.types.EqualsExpression;
import com.kush.lib.expressions.types.FieldExpression;
import com.kush.lib.expressions.types.GreaterThanEqualsExpression;
import com.kush.lib.expressions.types.GreaterThanExpression;
import com.kush.lib.expressions.types.LessThanEqualsExpression;
import com.kush.lib.expressions.types.LessThanExpression;
import com.kush.lib.expressions.types.NotExpression;
import com.kush.lib.expressions.types.OrExpression;

public class ExpressionCloner extends ExpressionProcessor<Expression> {

    private final ExpressionFactory expressionFactory;

    public ExpressionCloner(ExpressionFactory expressionFactory) {
        this.expressionFactory = expressionFactory;
    }

    @Override
    protected Expression handle(FieldExpression expression) {
        return expressionFactory.createFieldExpression(expression.getFieldName());
    }

    @Override
    protected Expression handle(AndExpression expression) throws ExpressionException {
        return expressionFactory.createAndExpression(process(expression.getLeft()), process(expression.getRight()));
    }

    @Override
    protected Expression handle(OrExpression expression) throws ExpressionException {
        return expressionFactory.createOrExpression(process(expression.getLeft()), process(expression.getRight()));
    }

    @Override
    protected Expression handle(NotExpression expression) throws ExpressionException {
        return expressionFactory.createNotExpression(process(expression.getChild()));
    }

    @Override
    protected Expression handle(EqualsExpression expression) throws ExpressionException {
        return expressionFactory.createEqualsExpression(process(expression.getLeft()), process(expression.getRight()));
    }

    @Override
    protected Expression handle(GreaterThanExpression expression) throws ExpressionException {
        return expressionFactory.createGreaterThanExpression(process(expression.getLeft()), process(expression.getRight()));
    }

    @Override
    protected Expression handle(GreaterThanEqualsExpression expression) throws ExpressionException {
        return expressionFactory.createGreaterThanEqualsExpression(process(expression.getLeft()), process(expression.getRight()));
    }

    @Override
    protected Expression handle(LessThanExpression expression) throws ExpressionException {
        return expressionFactory.createLessThanExpression(process(expression.getLeft()), process(expression.getRight()));
    }

    @Override
    protected Expression handle(LessThanEqualsExpression expression) throws ExpressionException {
        return expressionFactory.createLessThanEqualsExpression(process(expression.getLeft()), process(expression.getRight()));
    }

    @Override
    protected Expression handle(ConstantStringExpression expression) {
        return expressionFactory.createConstantStringExpression(expression.getValue());
    }

    @Override
    protected Expression handle(ConstantIntExpression expression) {
        return expressionFactory.createConstantIntExpression(expression.getValue());
    }
}
