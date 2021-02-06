package com.kush.lib.expressions.factory;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.ExpressionFactory;
import com.kush.lib.expressions.types.AndExpression;
import com.kush.lib.expressions.types.ConstantIntExpression;
import com.kush.lib.expressions.types.ConstantStringExpression;
import com.kush.lib.expressions.types.EqualsExpression;
import com.kush.lib.expressions.types.FieldExpression;
import com.kush.lib.expressions.types.GreaterThanExpression;
import com.kush.lib.expressions.types.NotExpression;
import com.kush.lib.expressions.types.OrExpression;

public class DefaultExpressionFactory implements ExpressionFactory {

    @Override
    public FieldExpression createFieldExpression(String fieldName) {
        return new DefaultFieldExpression(fieldName);
    }

    @Override
    public AndExpression createAndExpression(Expression leftExpr, Expression rightExpr) {
        return new DefaultAndExpression(leftExpr, rightExpr);
    }

    @Override
    public OrExpression createOrExpression(Expression leftExpr, Expression rightExpr) {
        return new DefaultOrExpression(leftExpr, rightExpr);
    }

    @Override
    public NotExpression createNotExpression(Expression childExpr) {
        return new DefaultNotExpression(childExpr);
    }

    @Override
    public EqualsExpression createEqualsExpression(Expression leftExpr, Expression rightExpr) {
        return new DefaultEqualsExpression(leftExpr, rightExpr);
    }

    @Override
    public GreaterThanExpression createGreaterThanExpression(Expression leftExpr, Expression rightExpr) {
        return new DefaultGreaterThanExpression(leftExpr, rightExpr);
    }

    @Override
    public ConstantStringExpression createConstantStringExpression(String value) {
        return new DefaultConstantStringExpression(value);
    }

    @Override
    public ConstantIntExpression createConstantIntExpression(int value) {
        return new DefaultConstantIntExpression(value);
    }
}
