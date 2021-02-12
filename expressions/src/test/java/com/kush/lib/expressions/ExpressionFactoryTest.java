package com.kush.lib.expressions;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.junit.Test;
import org.reflections.Reflections;

public class ExpressionFactoryTest {

    private static final String EXPR_PACKAGE = "com.kush.lib.expressions.clauses";

    @Test
    public void ensureFactoryHasAllCreateMethods() throws Exception {

        Map<String, Method> exprFactoryMethods = getAllExistingCreateMethods(ExpressionFactory.class);
        Set<Class<? extends Expression>> allExpressionClauses = getAllExpressionSubclasses();

        List<String> missingCreateMethods = new ArrayList<>();
        Map<String, Class<?>> wrongReturnTypes = new HashMap<>();
        for (Class<? extends Expression> exprClass : allExpressionClauses) {

            if (!exprClass.getName().startsWith(EXPR_PACKAGE)) {
                continue;
            }

            String createExprMethodName = getExpectedCreateMethodName(exprClass);
            Method createExprMethod = exprFactoryMethods.get(createExprMethodName);
            if (createExprMethod == null) {
                missingCreateMethods.add(createExprMethodName);
            } else {
                Class<?> returnType = createExprMethod.getReturnType();
                if (returnType != exprClass) {
                    wrongReturnTypes.put(createExprMethodName, returnType);
                }
            }
        }

        if (!missingCreateMethods.isEmpty() || !wrongReturnTypes.isEmpty()) {
            StringBuilder errorBuilder = new StringBuilder();
            if (!missingCreateMethods.isEmpty()) {
                errorBuilder = errorBuilder.append("No create method found for ")
                    .append(missingCreateMethods)
                    .append('\n');
            }
            if (!wrongReturnTypes.isEmpty()) {
                errorBuilder = errorBuilder.append("Wrong return type specified for ")
                    .append(wrongReturnTypes)
                    .append('\n');
            }
            fail(errorBuilder.toString());
        }
    }

    private String getExpectedCreateMethodName(Class<? extends Expression> exprClass) {
        String exprClassName = exprClass.getSimpleName();
        return "create" + exprClassName;
    }

    private Set<Class<? extends Expression>> getAllExpressionSubclasses() {
        Reflections reflections = new Reflections(EXPR_PACKAGE);
        Set<Class<? extends Expression>> allExpressionClauses = reflections.getSubTypesOf(Expression.class);
        return allExpressionClauses;
    }

    private Map<String, Method> getAllExistingCreateMethods(Class<ExpressionFactory> expressionFactoryClass) {
        Method[] allMethods = expressionFactoryClass.getDeclaredMethods();
        Map<String, Method> exprFactoryMethods = stream(allMethods)
            .collect(toMap(Method::getName, Function.identity()));
        return exprFactoryMethods;
    }
}
