package com.kush.lib;

import com.kush.lib.expressions.AndExpression;
import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.FieldExpression;
import com.kush.lib.expressions.NotExpression;
import com.kush.lib.expressions.OrExpression;

public interface ExpressionFactory {

    FieldExpression createFieldExpression(String fieldName);

    AndExpression createAndExpression(Expression leftExpr, Expression rightExpr);

    OrExpression createOrExpression(Expression leftExpr, Expression rightExpr);

    NotExpression createNotExpression(Expression childExpr);
}
