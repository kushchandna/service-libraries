package com.kush.lib.expressions.aspect;

import static com.kush.lib.expressions.utils.TypedResultFactory.nullableResult;
import static java.util.Collections.unmodifiableCollection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.kush.lib.expressions.Accessor;
import com.kush.lib.expressions.Type;

public class MapBasedAspect implements Aspect<Map<String, Object>> {

    private final Map<String, Field<Map<String, Object>>> fields;

    public MapBasedAspect(Map<String, Type> fieldTypes) {
        fields = new HashMap<>();
        fieldTypes.forEach((fieldName, fieldType) -> {
            MapBasedField field = new MapBasedField(fieldName, fieldType);
            fields.put(fieldName, field);
        });
    }

    @Override
    public Optional<Field<Map<String, Object>>> getField(String fieldName) {
        return Optional.ofNullable(fields.get(fieldName));
    }

    @Override
    public Collection<Field<Map<String, Object>>> getFields() {
        return unmodifiableCollection(fields.values());
    }

    private static class MapBasedField implements Field<Map<String, Object>> {

        private final String fieldName;
        private final Type fieldType;

        public MapBasedField(String field, Type fieldType) {
            fieldName = field;
            this.fieldType = fieldType;
        }

        @Override
        public Type getType() {
            return fieldType;
        }

        @Override
        public Accessor<Map<String, Object>> getAccessor() {
            return map -> nullableResult(map.get(fieldName), fieldType);
        }
    }
}
