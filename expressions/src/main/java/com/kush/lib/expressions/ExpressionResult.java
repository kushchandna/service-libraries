package com.kush.lib.expressions;

public interface ExpressionResult extends ExpressionTypeAware {

    boolean isNull();

    int getInt();

    double getDouble();

    float getFloat();

    long getLong();

    boolean getBoolean();

    String getString();

    Object getObject();
}
