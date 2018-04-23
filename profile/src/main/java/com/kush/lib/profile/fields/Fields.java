package com.kush.lib.profile.fields;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.kush.lib.profile.fields.validators.standard.TypeValidator;

public class Fields {

    public static FieldBuilder createTextFieldBuilder(String fieldName) {
        return createFieldBuilder(fieldName, String.class);
    }

    public static FieldBuilder createIntegerFieldBuilder(String fieldName) {
        return createFieldBuilder(fieldName, Integer.class);
    }

    public static FieldBuilder createNumericFieldBuilder(String fieldName) {
        return createFieldBuilder(fieldName, Double.class);
    }

    public static FieldBuilder createBooleanFieldBuilder(String fieldName) {
        return createFieldBuilder(fieldName, Boolean.class);
    }

    public static FieldBuilder createDateFieldBuilder(String fieldName) {
        return createFieldBuilder(fieldName, LocalDate.class);
    }

    public static FieldBuilder createDateTimeFieldBuilder(String fieldName) {
        return createFieldBuilder(fieldName, LocalDateTime.class);
    }

    private static <T> FieldBuilder createFieldBuilder(String fieldName, Class<T> type) {
        return new FieldBuilder(fieldName).addValidator(TypeValidator.forType(type));
    }
}
