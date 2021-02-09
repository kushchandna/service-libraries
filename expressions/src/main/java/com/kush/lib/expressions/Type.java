package com.kush.lib.expressions;

import java.util.Optional;

public enum Type {
    STRING,
    DOUBLE,
    FLOAT,
    LONG,
    INTEGER,
    BOOLEAN;

    public static Optional<Type> fromClass(Class<?> clazz) {
        return Optional.ofNullable(getType(clazz));
    }

    private static Type getType(Class<?> clazz) {
        if (clazz == String.class) {
            return STRING;
        } else if (clazz == int.class || clazz == Integer.class) {
            return INTEGER;
        } else if (clazz == long.class || clazz == Long.class) {
            return LONG;
        } else if (clazz == double.class || clazz == Double.class) {
            return DOUBLE;
        } else if (clazz == float.class || clazz == Float.class) {
            return FLOAT;
        } else if (clazz == boolean.class || clazz == Boolean.class) {
            return BOOLEAN;
        }
        return null;
    }
}
