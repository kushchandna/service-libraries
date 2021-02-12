package com.kush.lib.expressions.aspect;

import static com.kush.lib.expressions.types.factory.TypedValueFactory.nullableValue;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Map;

import com.kush.lib.expressions.Accessor;
import com.kush.lib.expressions.types.ImpactedByAutoBoxing;
import com.kush.lib.expressions.types.Type;

@ImpactedByAutoBoxing
class MapBasedAspect extends BaseAspect<Map<String, Object>> {

    public MapBasedAspect(Map<String, Type> fieldTypes) {
        super(createFields(fieldTypes));
    }

    private static Collection<Field<Map<String, Object>>> createFields(Map<String, Type> fieldTypes) {
        return fieldTypes.entrySet()
            .stream()
            .map(e -> createField(e.getKey(), e.getValue()))
            .collect(toList());
    }

    private static Field<Map<String, Object>> createField(String fieldName, Type fieldType) {
        Accessor<Map<String, Object>> accessor = map -> nullableValue(map.get(fieldName), fieldType);
        return new DefaultField<>(fieldName, fieldType, accessor);
    }
}
