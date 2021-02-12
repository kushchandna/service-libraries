package com.kush.lib.expressions.aspect;

import java.util.Map;

import com.kush.lib.expressions.types.ImpactedByAutoBoxing;
import com.kush.lib.expressions.types.Type;

public class Aspects {

    @ImpactedByAutoBoxing
    public static Aspect<Map<String, Object>> mapBased(Map<String, Type> fieldTypes) {
        return new MapBasedAspect(fieldTypes);
    }

    public static <T> Aspect<T> classBased(Class<T> clazz) {
        return new ClassBasedAspect<>(clazz);
    }
}
