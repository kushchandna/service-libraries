package com.kush.lib.expressions.types;

import java.util.Arrays;
import java.util.List;

public enum Type {

    BOOLEAN(boolean.class, Boolean.class),
    BYTE(byte.class, Byte.class),
    CHAR(char.class, Character.class),
    INTEGER(int.class, Integer.class),
    LONG(long.class, Long.class),
    FLOAT(float.class, Float.class),
    DOUBLE(double.class, Double.class),
    STRING(String.class),
    OBJECT;

    private List<Class<?>> javaClasses;

    private Type(Class<?>... javaClasses) {
        this.javaClasses = Arrays.asList(javaClasses);
    }

    public static Type forClass(Class<?> clazz) {
        for (Type type : values()) {
            for (Class<?> typeClass : type.javaClasses) {
                if (typeClass.isAssignableFrom(clazz)) {
                    return type;
                }
            }
        }
        return OBJECT;
    }
}
