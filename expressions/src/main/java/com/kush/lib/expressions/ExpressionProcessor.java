package com.kush.lib.expressions;

import static java.util.Collections.unmodifiableMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.kush.lib.expressions.types.AndExpression;
import com.kush.lib.expressions.types.ConstantIntExpression;
import com.kush.lib.expressions.types.ConstantStringExpression;
import com.kush.lib.expressions.types.FieldExpression;
import com.kush.lib.expressions.types.NotExpression;
import com.kush.lib.expressions.types.OrExpression;

public class ExpressionProcessor {

    private static Map<Class<?>, Method> processingMethods;

    private final ExpressionFactory expressionFactory;

    public ExpressionProcessor(ExpressionFactory expressionFactory) {
        this.expressionFactory = expressionFactory;
    }

    public final Expression process(Expression expression) throws ExpressionException {
        initializeProcessingMethodsIfRequired();
        return invokeSpecificProcessMethod(expression);
    }

    protected FieldExpression process(FieldExpression expression) {
        return expressionFactory.createFieldExpression(expression.getFieldName());
    }

    protected AndExpression process(AndExpression expression) throws ExpressionException {
        return expressionFactory.createAndExpression(process(expression.getLeft()), process(expression.getRight()));
    }

    protected OrExpression process(OrExpression expression) throws ExpressionException {
        return expressionFactory.createOrExpression(process(expression.getLeft()), process(expression.getRight()));
    }

    protected NotExpression process(NotExpression expression) throws ExpressionException {
        return expressionFactory.createNotExpression(process(expression.getChild()));
    }

    protected ConstantStringExpression process(ConstantStringExpression expression) {
        return expressionFactory.createConstantStringExpression(expression.getValue());
    }

    protected ConstantIntExpression process(ConstantIntExpression expression) {
        return expressionFactory.createConstantIntExpression(expression.getValue());
    }

    private void initializeProcessingMethodsIfRequired() {
        if (processingMethods == null) {
            synchronized (ExpressionProcessor.class) {
                if (processingMethods == null) {
                    processingMethods = loadProcessingMethods(ExpressionProcessor.class);
                }
            }
        }
    }

    private Map<Class<?>, Method> loadProcessingMethods(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        int len = methods.length;
        Map<Class<?>, Method> processMethodByClass = new HashMap<>(len);
        for (int i = 0; i < len; i++) {
            Method m = methods[i];
            String name = m.getName();

            int modifiers = m.getModifiers();
            if ("process".equals(name) && !isEntryMethod(modifiers)) {
                Class<?>[] parameterTypes = m.getParameterTypes();
                Class<?> paramType = parameterTypes[0];
                m.setAccessible(true);
                processMethodByClass.put(paramType, m);
            }
        }
        return unmodifiableMap(processMethodByClass);
    }

    private boolean isEntryMethod(int modifiers) {
        return Modifier.isPublic(modifiers) && Modifier.isFinal(modifiers);
    }

    private Method getProcessMethod(Class<?> exprType) {
        if (exprType == null) {
            return null;
        }
        if (processingMethods.containsKey(exprType)) {
            return processingMethods.get(exprType);
        }
        for (Class<?> interfaceVar : exprType.getInterfaces()) {
            Method method = processingMethods.get(interfaceVar);
            if (method != null) {
                return method;
            }
        }
        return getProcessMethod(exprType.getSuperclass());
    }

    private Expression invokeSpecificProcessMethod(Expression expression) throws ExpressionException {
        Class<?> exprType = expression.getClass();
        Method processMethod = getProcessMethod(exprType);
        if (processMethod == null) {
            return null;
        }
        try {
            return (Expression) processMethod.invoke(expression);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ExpressionException(e.getMessage(), e);
        }
    }
}
