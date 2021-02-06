package com.kush.lib.expressions;

public interface TypedResult extends Comparable<TypedResult> {

    boolean isNull();

    int getInt();

    double getDouble();

    float getFloat();

    long getLong();

    boolean getBoolean();

    String getString();

    Object getObject();

    Type getType();
}
