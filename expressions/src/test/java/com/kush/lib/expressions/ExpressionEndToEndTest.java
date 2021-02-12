package com.kush.lib.expressions;

import static com.kush.lib.expressions.types.Type.BOOLEAN;
import static com.kush.lib.expressions.types.Type.INTEGER;
import static com.kush.lib.expressions.types.factory.TypedValueFactory.booleanValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.kush.lib.expressions.aspect.Aspect;
import com.kush.lib.expressions.aspect.AspectFieldEvaluationFactory;
import com.kush.lib.expressions.aspect.Aspects;
import com.kush.lib.expressions.evaluators.DefaultExpressionEvaluatorFactory;
import com.kush.lib.expressions.factory.DefaultExpressionFactory;
import com.kush.lib.expressions.types.Type;
import com.kush.lib.expressions.types.TypedValue;

public class ExpressionEndToEndTest {

    @Test
    public void e2e_mapBased() throws Exception {
        Aspect<Map<String, Object>> aspect = Aspects.mapBased(ImmutableMap.of(
                "field1", INTEGER,
                "field2", INTEGER));
        String sql = "field1 = 1 and field2 = 2";
        runTest(aspect, sql, ImmutableMap.of(
                getMap(1, 2), true,
                getMap(3, 2), false,
                getMap(1, 3), false));
    }

    @Test
    public void e2e_classBased() throws Exception {
        Aspect<SampleObject> aspect = Aspects.classBased(SampleObject.class);
        String sql = "field1 = 1 and field2 = 2";
        runTest(aspect, sql, ImmutableMap.of(
                obj(1, 2), true,
                obj(3, 2), false,
                obj(1, 3), false));
    }

    private <T> void runTest(Aspect<T> aspect, String sql, Map<T, Boolean> objectVsExpectedResult) throws Exception {

        ExpressionParser<String> parser = createParser();
        Expression expression = parser.parse(sql);

        AspectFieldEvaluationFactory<T> fieldEvaluatorFactory = new AspectFieldEvaluationFactory<>(aspect);
        ExpressionEvaluatorFactory<T> evaluatorFactory = new DefaultExpressionEvaluatorFactory<>(fieldEvaluatorFactory);
        ExpressionEvaluator<T> evaluator = evaluatorFactory.create(expression);

        Type type = evaluator.evaluateType();
        assertThat(type, is(equalTo(BOOLEAN)));

        for (Entry<T, Boolean> entry : objectVsExpectedResult.entrySet()) {
            T testObject = entry.getKey();
            Boolean expectedResult = entry.getValue();

            TypedValue actualResult = evaluator.evaluate(testObject);
            assertThat(String.valueOf(testObject), actualResult, is(equalTo(booleanValue(expectedResult))));
        }
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

    private static SampleObject obj(int field1, int field2) {
        return new SampleObject(field1, field2);
    }

    private static class SampleObject {

        private final int field1;
        private final int field2;

        public SampleObject(int field1, int field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        @Override
        public String toString() {
            return "SampleObject [field1=" + field1 + ", field2=" + field2 + "]";
        }
    }
}
