package com.kush.lib.expressions.factory;

import java.util.Collection;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.ExpressionFactory;
import com.kush.lib.expressions.clauses.AndExpression;
import com.kush.lib.expressions.clauses.ConstantIntExpression;
import com.kush.lib.expressions.clauses.ConstantStringExpression;
import com.kush.lib.expressions.clauses.EqualsExpression;
import com.kush.lib.expressions.clauses.FieldExpression;
import com.kush.lib.expressions.clauses.GreaterThanEqualsExpression;
import com.kush.lib.expressions.clauses.GreaterThanExpression;
import com.kush.lib.expressions.clauses.InExpression;
import com.kush.lib.expressions.clauses.LessThanEqualsExpression;
import com.kush.lib.expressions.clauses.LessThanExpression;
import com.kush.lib.expressions.clauses.NotExpression;
import com.kush.lib.expressions.clauses.OrExpression;

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
    public InExpression createInExpression(Expression targetExpr, Collection<Expression> inExprs) {
        return new DefaultInExpression(targetExpr, inExprs);
    }

    @Override
    public GreaterThanExpression createGreaterThanExpression(Expression leftExpr, Expression rightExpr) {
        return new DefaultGreaterThanExpression(leftExpr, rightExpr);
    }

    @Override
    public GreaterThanEqualsExpression createGreaterThanEqualsExpression(Expression leftExpr, Expression rightExpr) {
        return new DefaultGreaterThanEqualsExpression(leftExpr, rightExpr);
    }

    @Override
    public LessThanExpression createLessThanExpression(Expression leftExpr, Expression rightExpr) {
        return new DefaultLessThanExpression(leftExpr, rightExpr);
    }

    @Override
    public LessThanEqualsExpression createLessThanEqualsExpression(Expression leftExpr, Expression rightExpr) {
        return new DefaultLessThanEqualsExpression(leftExpr, rightExpr);
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
