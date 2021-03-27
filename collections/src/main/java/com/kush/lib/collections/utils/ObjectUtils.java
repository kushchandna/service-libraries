package com.kush.lib.collections.utils;

import java.util.function.Function;

public class ObjectUtils {

    public static <T, R> R nullOrElse(T object, Function<T, R> function) {
        return object == null ? null : function.apply(object);
    }
}
