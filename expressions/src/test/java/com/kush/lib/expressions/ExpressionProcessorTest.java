package com.kush.lib.expressions;

import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isProtected;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.reflections.Reflections;

public class ExpressionProcessorTest {

    private static final String EXPR_PACKAGE = "com.kush.lib.expressions.clauses";

    @Test
    public void ensureProcessorHasAllHandleMethods() throws Exception {
        Set<Class<?>> existingHandleMethodParams = getAllExistingHandleMethodParams(ExpressionProcessor.class);
        List<String> missingHandleMethods = allExpressionSubclasses()
            .filter(exprClass -> !existingHandleMethodParams.contains(exprClass))
            .map(exprClass -> exprClass.getName())
            .collect(toList());
        if (!missingHandleMethods.isEmpty()) {
            StringBuilder errorBuilder = new StringBuilder();
            if (!missingHandleMethods.isEmpty()) {
                errorBuilder = errorBuilder.append("No handle method found for ")
                    .append(missingHandleMethods);
            }
            fail(errorBuilder.toString());
        }
    }

    private Stream<Class<? extends Expression>> allExpressionSubclasses() {
        Reflections reflections = new Reflections(EXPR_PACKAGE);
        return reflections.getSubTypesOf(Expression.class)
            .stream()
            .filter(exprClass -> exprClass.getName().startsWith(EXPR_PACKAGE));
    }

    private Set<Class<?>> getAllExistingHandleMethodParams(Class<?> expressionProcessorClass) {
        Method[] allMethods = expressionProcessorClass.getDeclaredMethods();
        return stream(allMethods)
            .filter(ExpressionProcessorTest::isAbstractHandleMethod)
            .map(method -> method.getParameterTypes()[0])
            .collect(Collectors.toSet());
    }

    private static boolean isAbstractHandleMethod(Method method) {
        if (!method.getName().equals("handle")) {
            return false;
        }
        if (method.getParameterCount() > 1) {
            return false;
        }
        int modifiers = method.getModifiers();
        return isAbstract(modifiers) && isProtected(modifiers);
    }
}
