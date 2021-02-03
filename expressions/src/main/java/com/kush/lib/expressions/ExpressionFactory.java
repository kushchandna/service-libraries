package com.kush.lib.expressions;

import com.kush.lib.expressions.types.AndExpression;
import com.kush.lib.expressions.types.FieldExpression;
import com.kush.lib.expressions.types.NotExpression;
import com.kush.lib.expressions.types.OrExpression;

public interface ExpressionFactory {

    FieldExpression createFieldExpression(String fieldName);

    AndExpression createAndExpression(Expression leftExpr, Expression rightExpr);

    OrExpression createOrExpression(Expression leftExpr, Expression rightExpr);

    NotExpression createNotExpression(Expression childExpr);
}
