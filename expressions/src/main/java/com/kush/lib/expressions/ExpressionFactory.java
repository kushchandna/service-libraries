package com.kush.lib.expressions;

import java.util.Collection;

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

public interface ExpressionFactory {

    FieldExpression createFieldExpression(String fieldName);

    AndExpression createAndExpression(Expression leftExpr, Expression rightExpr);

    OrExpression createOrExpression(Expression leftExpr, Expression rightExpr);

    NotExpression createNotExpression(Expression childExpr);

    EqualsExpression createEqualsExpression(Expression leftExpr, Expression rightExpr);

    InExpression createInExpression(Expression targetExpr, Collection<Expression> inExprs);

    GreaterThanExpression createGreaterThanExpression(Expression leftExpr, Expression rightExpr);

    GreaterThanEqualsExpression createGreaterThanEqualsExpression(Expression leftExpr, Expression rightExpr);

    LessThanExpression createLessThanExpression(Expression leftExpr, Expression rightExpr);

    LessThanEqualsExpression createLessThanEqualsExpression(Expression leftExpr, Expression rightExpr);

    ConstantStringExpression createConstantStringExpression(String value);

    ConstantIntExpression createConstantIntExpression(int value);
}
