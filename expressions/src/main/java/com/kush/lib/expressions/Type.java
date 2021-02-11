package com.kush.lib.expressions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Type {
    STRING(String.class),
    DOUBLE(double.class, Double.class),
    FLOAT(float.class, Float.class),
    LONG(long.class, Long.class),
    INTEGER(int.class, Integer.class),
    BOOLEAN(boolean.class, Boolean.class);

    private List<Class<?>> javaClasses;

    private Type(Class<?>... javaClasses) {
        this.javaClasses = Arrays.asList(javaClasses);
    }

    public static Optional<Type> forClass(Class<?> clazz) {
        return Optional.ofNullable(getType(clazz));
    }

    private static Type getType(Class<?> clazz) {
        for (Type type : values()) {
            for (Class<?> typeClass : type.javaClasses) {
                if (typeClass.isAssignableFrom(clazz)) {
                    return type;
                }
            }
        }
        return null;
    }
}
