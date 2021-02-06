package com.kush.lib.expressions;

import static com.kush.lib.expressions.Type.BOOLEAN;
import static com.kush.lib.expressions.Type.INTEGER;
import static com.kush.lib.expressions.utils.TypedResultFactory.booleanResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.kush.lib.expressions.aspect.Aspect;
import com.kush.lib.expressions.aspect.AspectFieldEvaluationFactory;
import com.kush.lib.expressions.aspect.MapBasedAspect;
import com.kush.lib.expressions.evaluators.DefaultExpressionEvaluatorFactory;
import com.kush.lib.expressions.factory.DefaultExpressionFactory;

public class ExpressionEndToEndTest {

    @Test
    public void e2e() throws Exception {
        String sql = "field1 = 1 and field2 = 2";

        ExpressionParser<String> parser = createParser();
        Expression expression = parser.parse(sql);

        Aspect<Map<String, Object>> aspect = new MapBasedAspect(ImmutableMap.of(
                "field1", INTEGER,
                "field2", INTEGER));
        AspectFieldEvaluationFactory<Map<String, Object>> fieldEvaluatorFactory = new AspectFieldEvaluationFactory<>(aspect);
        ExpressionEvaluatorFactory<Map<String, Object>> evaluatorFactory =
                new DefaultExpressionEvaluatorFactory<>(fieldEvaluatorFactory);
        ExpressionEvaluator<Map<String, Object>> evaluator = evaluatorFactory.create(expression);

        Type type = evaluator.evaluateType();
        assertThat(type, is(equalTo(BOOLEAN)));

        TypedResult result1 = evaluator.evaluate(getMap(1, 2));
        assertThat(result1, is(equalTo(booleanResult(true))));

        TypedResult result2 = evaluator.evaluate(getMap(3, 2));
        assertThat(result2, is(equalTo(booleanResult(false))));

        TypedResult result3 = evaluator.evaluate(getMap(1, 3));
        assertThat(result3, is(equalTo(booleanResult(false))));
    }

    private Map<String, Object> getMap(int field1, int field2) {
        Map<String, Object> fieldValueMap = new HashMap<>();
        fieldValueMap.put("field1", field1);
        fieldValueMap.put("field2", field2);
        return fieldValueMap;
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

            Expression leftExpr1 = expressionFactory.createFieldExpression("field1");
            Expression rightExpr1 = expressionFactory.createConstantIntExpression(1);
            Expression leftExpr2 = expressionFactory.createFieldExpression("field2");
            Expression rightExpr2 = expressionFactory.createConstantIntExpression(2);

            Expression leftExpr = expressionFactory.createEqualsExpression(leftExpr1, rightExpr1);
            Expression rightExpr = expressionFactory.createEqualsExpression(leftExpr2, rightExpr2);
            return expressionFactory.createAndExpression(leftExpr, rightExpr);
        }
    }
}
