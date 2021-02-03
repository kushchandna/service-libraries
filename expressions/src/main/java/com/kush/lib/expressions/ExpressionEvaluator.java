package com.kush.lib.expressions;

public interface ExpressionEvaluator<T> {

    int evaluateInt(T object) throws ExpressionException;

    double evaluateDouble(T object) throws ExpressionException;

    float evaluateFloat(T object) throws ExpressionException;

    long evaluateLong(T object) throws ExpressionException;

    String evaluateString(T object) throws ExpressionException;

    boolean evaluateBoolean(T object) throws ExpressionException;
}
