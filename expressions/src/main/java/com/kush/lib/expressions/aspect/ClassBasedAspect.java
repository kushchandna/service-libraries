package com.kush.lib.expressions.aspect;

import static com.kush.lib.expressions.Type.STRING;
import static com.kush.lib.expressions.utils.TypedValueFactory.booleanValue;
import static com.kush.lib.expressions.utils.TypedValueFactory.doubleValue;
import static com.kush.lib.expressions.utils.TypedValueFactory.floatValue;
import static com.kush.lib.expressions.utils.TypedValueFactory.intValue;
import static com.kush.lib.expressions.utils.TypedValueFactory.longValue;
import static com.kush.lib.expressions.utils.TypedValueFactory.nullValue;
import static com.kush.lib.expressions.utils.TypedValueFactory.stringValue;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import com.kush.lib.expressions.AccessException;
import com.kush.lib.expressions.Accessor;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.Type;
import com.kush.lib.expressions.TypedValue;
import com.kush.lib.expressions.handler.TypeHandler;

class ClassBasedAspect<T> extends BaseAspect<T> {

    public ClassBasedAspect(Class<T> clazz) {
        super(createFields(clazz));
    }

    private static <T> Collection<Field<T>> createFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
            .map(f -> ClassBasedAspect.<T>createField(f))
            .filter(Objects::nonNull)
            .collect(toList());
    }

    private static <T> Field<T> createField(java.lang.reflect.Field classField) {
        classField.setAccessible(true);
        String fieldName = classField.getName();
        Optional<Type> fieldType = Type.fromClass(classField.getType());
        Accessor<T> accessor = new Accessor<T>() {

            @Override
            public TypedValue access(T object) throws AccessException {
                ClassFieldToTypedResultHandler handler = new ClassFieldToTypedResultHandler(classField, object);
                try {
                    return handler.handle(fieldType.get());
                } catch (ExpressionException e) {
                    throw new AccessException(e.getMessage(), e);
                }
            }
        };
        Field<T> field = null;
        if (fieldType.isPresent()) {
            field = new DefaultField<>(fieldName, fieldType.get(), accessor);
        }
        return field;
    }

    private static class ClassFieldToTypedResultHandler extends TypeHandler<TypedValue> {

        private final java.lang.reflect.Field classField;
        private final Object object;

        public ClassFieldToTypedResultHandler(java.lang.reflect.Field classField, Object object) {
            this.classField = classField;
            this.object = object;
        }

        @Override
        protected TypedValue handleString() throws ExpressionException {
            try {
                Object value = classField.get(object);
                if (value == null) {
                    return nullValue(STRING);
                } else {
                    return stringValue((String) value);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ExpressionException(e.getMessage(), e);
            }
        }

        @Override
        protected TypedValue handleLong() throws ExpressionException {
            try {
                long value = classField.getLong(object);
                return longValue(value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ExpressionException(e.getMessage(), e);
            }
        }

        @Override
        protected TypedValue handleInteger() throws ExpressionException {
            try {
                int value = classField.getInt(object);
                return intValue(value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ExpressionException(e.getMessage(), e);
            }
        }

        @Override
        protected TypedValue handleFloat() throws ExpressionException {
            try {
                float value = classField.getFloat(object);
                return floatValue(value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ExpressionException(e.getMessage(), e);
            }
        }

        @Override
        protected TypedValue handleDouble() throws ExpressionException {
            try {
                double value = classField.getDouble(object);
                return doubleValue(value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ExpressionException(e.getMessage(), e);
            }
        }

        @Override
        protected TypedValue handleBoolean() throws ExpressionException {
            try {
                boolean value = classField.getBoolean(object);
                return booleanValue(value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ExpressionException(e.getMessage(), e);
            }
        }
    }
}
