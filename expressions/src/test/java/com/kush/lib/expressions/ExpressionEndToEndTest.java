package com.kush.lib.expressions;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.kush.lib.expressions.evaluators.DefaultExpressionEvaluatorFactory;
import com.kush.lib.expressions.factory.DefaultExpressionFactory;

public class ExpressionEndToEndTest {

    @Test
    public void e2e() throws Exception {
        String sql = "field1 = 1 and field2 = 2";

        ExpressionParser<String> parser = createParser();
        Expression expression = parser.parse(sql);

        Map<String, Object> fieldValueMap = new HashMap<>();
        fieldValueMap.put("field1", 1);
        fieldValueMap.put("field2", 2);

        ExpressionEvaluatorFactory<Map<String, Object>> evaluatorFactory = createEvaluatorFactory();
        ExpressionEvaluator<Map<String, Object>> evaluator = evaluatorFactory.create(expression);

        boolean value = evaluator.evaluateBoolean(fieldValueMap);

        System.out.println(value);
    }

    private static ExpressionEvaluatorFactory<Map<String, Object>> createEvaluatorFactory() {
        return new DefaultExpressionEvaluatorFactory<>();
    }

    private static ExpressionParser<String> createParser() {
        return new FixedSqlParser();
    }

    private static class FixedSqlParser implements ExpressionParser<String> {

        private final ExpressionFactory expressionFactory;

        public FixedSqlParser() {
            expressionFactory = new DefaultExpressionFactory();
        }

        @Override
        public Expression parse(String input) throws ExpressionParsingFailedException {
            if (!"field1 = 1 and field2 = 2".equals(input)) {
                throw new ExpressionParsingFailedException();
            }

            Expression leftExpr2 = expressionFactory.createFieldExpression("field1");
            Expression rightExpr2 = expressionFactory.createConstantIntExpression(1);
            Expression leftExpr3 = expressionFactory.createFieldExpression("field2");
            Expression rightExpr3 = expressionFactory.createConstantIntExpression(2);

            Expression leftExpr1 = expressionFactory.createEqualsExpression(leftExpr2, rightExpr2);
            Expression rightExpr1 = expressionFactory.createEqualsExpression(leftExpr3, rightExpr3);
            return expressionFactory.createAndExpression(leftExpr1, rightExpr1);
        }
    }
}
