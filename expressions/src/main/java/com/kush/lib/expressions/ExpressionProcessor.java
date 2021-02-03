package com.kush.lib.expressions;

import static java.util.Collections.unmodifiableMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.kush.lib.expressions.types.AndExpression;
import com.kush.lib.expressions.types.ConstantIntExpression;
import com.kush.lib.expressions.types.ConstantStringExpression;
import com.kush.lib.expressions.types.EqualsExpression;
import com.kush.lib.expressions.types.FieldExpression;
import com.kush.lib.expressions.types.NotExpression;
import com.kush.lib.expressions.types.OrExpression;

public abstract class ExpressionProcessor<T> {

    private static Map<Class<?>, Method> handlingMethods;

    public final T process(Expression expression) throws ExpressionException {
        initializeHandlingMethodsIfRequired();
        return invokeSpecificProcessMethod(expression);
    }

    protected abstract T handle(FieldExpression expression) throws ExpressionException;

    protected abstract T handle(AndExpression expression) throws ExpressionException;

    protected abstract T handle(OrExpression expression) throws ExpressionException;

    protected abstract T handle(NotExpression expression) throws ExpressionException;

    protected abstract T handle(EqualsExpression expression) throws ExpressionException;

    protected abstract T handle(ConstantStringExpression expression) throws ExpressionException;

    protected abstract T handle(ConstantIntExpression expression) throws ExpressionException;

    private void initializeHandlingMethodsIfRequired() {
        if (handlingMethods == null) {
            handlingMethods = loadHandlingMethods(ExpressionProcessor.class);
        }
    }

    private Map<Class<?>, Method> loadHandlingMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        int len = methods.length;
        Map<Class<?>, Method> processMethodByClass = new HashMap<>(len);
        for (int i = 0; i < len; i++) {
            Method m = methods[i];
            String name = m.getName();

            if ("handle".equals(name)) {
                Class<?>[] parameterTypes = m.getParameterTypes();
                Class<?> paramType = parameterTypes[0];
                m.setAccessible(true);
                processMethodByClass.put(paramType, m);
            }
        }
        return unmodifiableMap(processMethodByClass);
    }

    private Method getHandlerMethod(Class<?> exprType) {
        if (exprType == null) {
            return null;
        }
        if (handlingMethods.containsKey(exprType)) {
            return handlingMethods.get(exprType);
        }


        for (Class<?> interfaceVar : exprType.getInterfaces()) {
            Method method = handlingMethods.get(interfaceVar);
            if (method != null) {
                return method;
            }
        }
        return getHandlerMethod(exprType.getSuperclass());
    }

    @SuppressWarnings("unchecked")
    private T invokeSpecificProcessMethod(Expression expression) throws ExpressionException {
        Class<?> exprType = expression.getClass();
        Method handleMethod = getHandlerMethod(exprType);
        if (handleMethod == null) {
            return null;
        }
        try {
            return (T) handleMethod.invoke(this, expression);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ExpressionException(e.getMessage(), e);
        }
    }
}
