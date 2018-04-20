package com.kush.lib.profile.fields;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Fields {

    public static FieldBuilder<String> createTextFieldBuilder(String fieldName) {
        return createFieldBuilder(fieldName);
    }

    public static FieldBuilder<Integer> createIntegerFieldBuilder(String fieldName) {
        return createFieldBuilder(fieldName);
    }

    public static FieldBuilder<Double> createNumericFieldBuilder(String fieldName) {
        return createFieldBuilder(fieldName);
    }

    public static FieldBuilder<Boolean> createBooleanFieldBuilder(String fieldName) {
        return createFieldBuilder(fieldName);
    }

    public static FieldBuilder<LocalDate> createDateFieldBuilder(String fieldName) {
        return createFieldBuilder(fieldName);
    }

    public static FieldBuilder<LocalDateTime> createDateTimeFieldBuilder(String fieldName) {
        return createFieldBuilder(fieldName);
    }

    private static <T> FieldBuilder<T> createFieldBuilder(String fieldName) {
        return new FieldBuilder<>(fieldName);
    }
}
