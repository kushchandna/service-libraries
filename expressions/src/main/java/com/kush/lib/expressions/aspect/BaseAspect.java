package com.kush.lib.expressions.aspect;

import static java.util.Collections.unmodifiableCollection;
import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class BaseAspect<T> implements Aspect<T> {

    private final Map<String, Field<T>> fields;

    public BaseAspect(Collection<Field<T>> fields) {
        this.fields = new HashMap<>(fields.stream()
            .collect(toMap(f -> f.getName(), Function.identity())));
    }

    @Override
    public Optional<Field<T>> getField(String fieldName) {
        return Optional.ofNullable(fields.get(fieldName));
    }

    @Override
    public Collection<Field<T>> getFields() {
        return unmodifiableCollection(fields.values());
    }
}
