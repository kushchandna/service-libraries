package com.kush.lib.expressions.types;

public interface TypedValue extends Comparable<TypedValue> {

    Type getType();

    boolean isNull();

    boolean getBoolean();

    int getInt();

    long getLong();

    float getFloat();

    double getDouble();

    String getString();

    Object getObject();
}
