package com.kush.lib.expressions.types;

public interface TypedValue extends Comparable<TypedValue> {

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
